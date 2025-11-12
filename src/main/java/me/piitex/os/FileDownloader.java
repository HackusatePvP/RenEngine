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

    /**
     * Initializes the FileDownloader and its thread pool.
     */
    public FileDownloader() {

    }

    /**
     * Starts the download task asynchronously.
     * @param fileUrl The URL of the file to download.
     * @param outputFile The destination of the file.
     */
    public void startDownload(String fileUrl, File outputFile) {
        logger.info("Submitting download task for: {}", fileUrl);

        if (Platform.isFxApplicationThread()) {
            Exception exception = new InvalidDownloadThreadException();
            logger.error(exception.getMessage(), exception);
            return;
        }

        performDownload(fileUrl, outputFile);
    }

    private void performDownload(String fileUrl, File outputFile) {
        DownloadInfo info = null;
        try {
            URL url = new URI(fileUrl).toURL();
            URLConnection connection = url.openConnection();
            connection.setConnectTimeout(5000);
            requestProperties.forEach(connection::setRequestProperty);

            activeConnections.put(fileUrl, connection); // Store the connection

            long fileSize = connection.getContentLengthLong();
            String fileName = url.getFile().substring(url.getFile().lastIndexOf('/') + 1);

            // Initialize Info and Report Start
            info = new DownloadInfo(fileName, fileSize, fileUrl);
            activeDownloads.put(fileUrl, info); // Track active download
            handleStart(info);

            //
            try (InputStream inputStream = connection.getInputStream();
                 FileOutputStream outputStream = new FileOutputStream(outputFile)) {

                byte[] buffer = new byte[BUFFER_SIZE];
                int bytesRead;
                long totalBytesDownloaded = 0;
                long lastNotificationTime = System.currentTimeMillis();

                // Loop the download bytes until completed.
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                    totalBytesDownloaded += bytesRead;
                    info.setDownloadedBytes(totalBytesDownloaded);

                    // Throttle progress updates
                    if (System.currentTimeMillis() - lastNotificationTime > 200) {
                        handleProgress(info);
                        lastNotificationTime = System.currentTimeMillis();
                    }
                }

                info.setComplete(true);
                handleProgress(info); // Final progress update
                handleComplete(info, outputFile);

            } catch (IOException e) {
                String message = e.getMessage() != null ? e.getMessage().toLowerCase() : "";

                // Check for controlled cancellation errors (stream/socket closure)
                // These messages signal that the stream was closed by cancelDownload(url)
                if (message.contains("socket closed") || message.contains("stream closed") || message.contains("operation canceled")) {
                    // This is a controlled stop. We don't treat it as a failure.
                    // The handleCancel event has already fired.
                    logger.info("Download task for {} stopped by user or interruption.", info.getFileName());
                } else {
                    handleError(info, e);
                }
            }

        } catch (Exception e) {
            // URL/Connection setup error
            if (info == null) {
                info = new DownloadInfo("Unknown", -1, fileUrl);
            }
            handleError(info, e);
        } finally {
            if (info != null) {
                activeDownloads.remove(info.getDownloadUrl());
                activeConnections.remove(info.getDownloadUrl());
            }
        }
    }

    public boolean cancelDownload(String fileUrl) {
        DownloadInfo info = activeDownloads.remove(fileUrl);
        URLConnection connection = activeConnections.remove(fileUrl);

        // Force the stream/connection to close to unstick the I/O thread
        if (connection != null) {
            try {
                if (connection instanceof HttpURLConnection) {
                    ((HttpURLConnection) connection).disconnect();
                }
            } catch (Exception ignored) {
                // Ignore any exception on forced close
            }
        }

        // Interrupt the thread
        boolean wasRunning = false;

        // Call the cancel listeners
        if (info != null) {
            handleCancel(info);
        }

        return wasRunning;
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
        logger.info("Total Size: {}", (info.getTotalFileSize() > 0 ? info.getTotalFileSize() + "bytes" : "Unknown"));
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

    /**
     * Gathers and returns the download info for a specific file URL.
     * @param fileUrl The unique identifier (URL) of the download task.
     * @return The DownloadInfo object for that task, or null if it was never started.
     */
    public DownloadInfo getDownloadInfo(String fileUrl) {
        return activeDownloads.get(fileUrl);
    }

    /**
     * @return A map of all tracked downloads (active, completed, or failed).
     */
    public ConcurrentHashMap<String, DownloadInfo> getAllDownloadStatuses() {
        return activeDownloads;
    }

    /**
     * Shuts down the executor service cleanly. Should be called when the application exits.
     */
    public void shutdown() {
        activeDownloads.clear();
        listeners.clear();
        logger.info("Downloader service shut down.");
    }
}