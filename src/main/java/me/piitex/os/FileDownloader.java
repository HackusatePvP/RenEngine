package me.piitex.os;

import javafx.application.Platform;
import me.piitex.os.exceptions.InvalidDownloadThreadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;

public class FileDownloader {
    private static final int BUFFER_SIZE = 4096;
    private static final Logger logger = LoggerFactory.getLogger(FileDownloader.class);
    private final ConcurrentHashMap<String, DownloadInfo> activeDownloads = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, URLConnection> activeConnections = new ConcurrentHashMap<>();
    private final Set<DownloadListener> listeners = Collections.newSetFromMap(new ConcurrentHashMap<>());
    private final Map<String, String> requestProperties = new HashMap<>();
    private final ExecutorService executorService;

    /**
     * Initializes the FileDownloader and its thread pool.
     */
    public FileDownloader() {
        this.executorService = Executors.newCachedThreadPool();
    }

    /**
     * Initializes the FileDownloader and adds GitHub API support.
     * @param githubUrl Set to true to use GitHub API streams.
     */
    public FileDownloader(boolean githubUrl) {
        this.executorService = Executors.newCachedThreadPool();
        if (githubUrl) {
            addRequestProperty("Accept", "application/octet-stream");
        }
    }

    /**
     * Starts the download task asynchronously without hash checking.
     * @param fileUrl The URL of the file to download.
     * @param outputFile The destination of the file.
     */
    public void startDownload(String fileUrl, File outputFile) {
        startDownload(fileUrl, outputFile, null, null);
    }

    /**
     * Starts the download task asynchronously with security hash checking.
     * @param fileUrl The URL of the file to download.
     * @param outputFile The destination of the file.
     * @param expectedHash The expected checksum hash of the file (e.g., SHA-256).
     * @param hashAlgorithm The algorithm used for the hash (e.g., "SHA-256", "MD5"). Defaults to SHA-256 if null.
     */
    public void startDownload(String fileUrl, File outputFile, String expectedHash, String hashAlgorithm) {
        logger.info("Submitting download task for: {}", fileUrl);

        // Call on the FX Thread
        if (Platform.isFxApplicationThread()) {
            Exception exception = new InvalidDownloadThreadException();
            logger.error(exception.getMessage(), exception);
            return;
        }

        executorService.submit(() -> performDownload(fileUrl, outputFile, expectedHash, hashAlgorithm));
    }

    private void performDownload(String fileUrl, File outputFile, String expectedHash, String hashAlgorithm) {
        DownloadInfo info = null;
        try {
            URL url = new URI(fileUrl).toURL();
            URLConnection connection = url.openConnection();
            connection.setConnectTimeout(5000);

            requestProperties.forEach(connection::setRequestProperty);

            // Disable automatic redirects to handle them manually for ALL domains
            if (connection instanceof HttpURLConnection) {
                ((HttpURLConnection) connection).setInstanceFollowRedirects(false);
            }

            int redirects = 0;
            while (connection instanceof HttpURLConnection) {
                HttpURLConnection httpConn = (HttpURLConnection) connection;
                int status = httpConn.getResponseCode();

                if (status == HttpURLConnection.HTTP_MOVED_TEMP ||
                        status == HttpURLConnection.HTTP_MOVED_PERM ||
                        status == HttpURLConnection.HTTP_SEE_OTHER ||
                        status == 307 || status == 308) {

                    String redirectUrl = httpConn.getHeaderField("Location");
                    httpConn.disconnect();

                    @SuppressWarnings("deprecation")
                    URL newUrl = new URL(redirectUrl);
                    url = newUrl; // Update the outer scope url variable

                    connection = url.openConnection();
                    connection.setConnectTimeout(5000);

                    if (connection instanceof HttpURLConnection) {
                        HttpURLConnection newHttpConn = (HttpURLConnection) connection;
                        newHttpConn.setInstanceFollowRedirects(false);

                        // Override System JDK defaults and carry over the required Accept header
                        newHttpConn.setRequestProperty("User-Agent", "Mozilla/5.0");
                        if (requestProperties.containsKey("Accept")) {
                            newHttpConn.setRequestProperty("Accept", requestProperties.get("Accept"));
                        }
                    }

                    redirects++;
                    if (redirects > 5) {
                        throw new IOException("Too many redirects");
                    }
                } else {
                    break;
                }
            }

            // Connection is now finalized on the actual binary file
            long fileSize = connection.getContentLengthLong();

            // Use getPath() instead of getFile() to strip out "?download=true"
            String fileName = url.getPath().substring(url.getPath().lastIndexOf('/') + 1);

            info = new DownloadInfo(fileName, outputFile, fileSize, fileUrl);
            activeDownloads.put(fileUrl, info);
            handleStart(info);

            // Ensure the parent directories exist before writing
            File parentDir = outputFile.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                if (!parentDir.mkdirs()) {
                    throw new IOException("Failed to create parent directories for output file.");
                }
            }

            try (InputStream inputStream = connection.getInputStream();
                 FileOutputStream outputStream = new FileOutputStream(outputFile)) {

                byte[] buffer = new byte[BUFFER_SIZE];
                int bytesRead;
                long totalBytesDownloaded = 0;
                long lastNotificationTime = System.currentTimeMillis();

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                    totalBytesDownloaded += bytesRead;
                    info.setDownloadedBytes(totalBytesDownloaded);

                    if (System.currentTimeMillis() - lastNotificationTime > 200) {
                        handleProgress(info);
                        lastNotificationTime = System.currentTimeMillis();
                    }
                }

            } catch (IOException e) {
                String message = e.getMessage() != null ? e.getMessage().toLowerCase() : "";

                if (message.contains("socket closed") || message.contains("stream closed") || message.contains("operation canceled")) {
                    logger.info("Download task for {} stopped by user or interruption.", info.getFileName());
                    return; // Exit early since it was cancelled
                } else {
                    throw e; // Rethrow to be caught by the outer catch block
                }
            }

            if (expectedHash != null && !expectedHash.trim().isEmpty()) {
                String algorithm = (hashAlgorithm != null && !hashAlgorithm.trim().isEmpty()) ? hashAlgorithm : "SHA-256";
                logger.info("Verifying file integrity using {}...", algorithm);

                boolean isHashValid = verifyFileHash(outputFile, expectedHash, algorithm);
                if (!isHashValid) {
                    if (outputFile.exists() && !outputFile.delete()) {
                        logger.warn("Failed to delete tampered file: {}", outputFile.getAbsolutePath());
                    }
                    throw new SecurityException("Hash mismatch! The file may be corrupted or maliciously altered.");
                }
                logger.info("File integrity verified successfully.");
            }

            info.setComplete(true);
            handleProgress(info); // Final progress update
            handleComplete(info, outputFile);

        } catch (Exception e) {
            if (info == null) {
                info = new DownloadInfo("Unknown", outputFile, -1, fileUrl);
            }
            handleError(info, e);
        } finally {
            if (info != null) {
                activeDownloads.remove(info.getDownloadUrl());
                activeConnections.remove(info.getDownloadUrl());
            }
        }
    }

    /**
     * Verifies the cryptographic hash of a downloaded file.
     */
    private boolean verifyFileHash(File file, String expectedHash, String algorithm) throws Exception {
        MessageDigest digest = MessageDigest.getInstance(algorithm);

        try (InputStream fis = new FileInputStream(file)) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                digest.update(buffer, 0, bytesRead);
            }
        }

        byte[] hashBytes = digest.digest();
        StringBuilder hexString = new StringBuilder();

        for (byte b : hashBytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }

        return hexString.toString().equalsIgnoreCase(expectedHash);
    }

    public boolean cancelDownload(String fileUrl) {
        DownloadInfo info = activeDownloads.remove(fileUrl);
        URLConnection connection = activeConnections.remove(fileUrl);

        if (connection != null) {
            try {
                if (connection instanceof HttpURLConnection) {
                    ((HttpURLConnection) connection).disconnect();
                }
            } catch (Exception ignored) {
            }
        }

        if (info != null) {
            handleCancel(info);
            return true;
        }

        return false;
    }

    public long getRemoteFileSize(String fileUrl) throws IOException {
        URL url = new URL(fileUrl);
        URLConnection connection = url.openConnection();
        connection.setConnectTimeout(5000);
        if (connection instanceof HttpURLConnection) {
            ((HttpURLConnection) connection).setRequestMethod("HEAD");
        }
        connection.connect();

        return connection.getContentLengthLong();
    }

    public void addRequestProperty(String key, String value) {
        requestProperties.put(key, value);
    }

    public void addDownloadListener(DownloadListener listener) {
        this.listeners.add(listener);
        logger.debug("DownloadListener added: {}", listener.getClass().getSimpleName());
    }

    public void removeDownloadListener(DownloadListener listener) {
        this.listeners.remove(listener);
        logger.debug("DownloadListener removed: {}", listener.getClass().getSimpleName());
    }

    private void handleStart(DownloadInfo info) {
        logger.info("Download Started: {}", info.getFileName());
        logger.info("Total Size: {} bytes", (info.getTotalFileSize() > 0 ? info.getTotalFileSize() : "Unknown"));
        for (DownloadListener listener : listeners) {
            listener.onDownloadStart(info);
        }
    }

    private void handleProgress(DownloadInfo info) {
        for (DownloadListener listener : listeners) {
            listener.onDownloadProgress(info);
        }
    }

    private void handleComplete(DownloadInfo info, File outputFile) {
        logger.info("Download Complete! {}", info.getFileName());
        logger.info("File saved to: {}", outputFile);
        for (DownloadListener listener : listeners) {
            listener.onDownloadComplete(info, outputFile);
        }
    }

    private void handleError(DownloadInfo info, Exception e) {
        logger.error("Download Failed for {}", info.getFileName(), e);
        for (DownloadListener listener : listeners) {
            listener.onDownloadError(info, e);
        }
    }

    private void handleCancel(DownloadInfo info) {
        logger.info("Download Cancelled: {}", info.getFileName());
        for (DownloadListener listener : listeners) {
            listener.onDownloadCancel(info);
        }
    }


    public DownloadInfo getDownloadInfo(String fileUrl) {
        return activeDownloads.get(fileUrl);
    }

    public ConcurrentHashMap<String, DownloadInfo> getAllDownloadStatuses() {
        return activeDownloads;
    }

    public Set<DownloadListener> getListeners() {
        return listeners;
    }

    /**
     * Shuts down the executor service cleanly. Should be called when the application exits.
     */
    public void shutdown() {
        activeDownloads.clear();
        listeners.clear();

        // Shut down the thread pool to prevent application hang on exit
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdownNow();
        }

        logger.info("Downloader service shut down.");
    }
}