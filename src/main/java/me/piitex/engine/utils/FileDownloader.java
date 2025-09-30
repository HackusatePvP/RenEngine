package me.piitex.engine.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FileDownloader {
    private final ExecutorService executor;
    private static final int BUFFER_SIZE = 4096;
    private static final Logger logger = LoggerFactory.getLogger(FileDownloader.class);
    private final ConcurrentHashMap<String, DownloadInfo> activeDownloads = new ConcurrentHashMap<>();
    private final Set<DownloadListener> listeners = Collections.newSetFromMap(new ConcurrentHashMap<>());

    /**
     * Initializes the FileDownloader and its thread pool.
     */
    public FileDownloader() {
        // Use a single-threaded executor to run downloads sequentially
        this.executor = Executors.newSingleThreadExecutor(); 
    }

    /**
     * Starts the download task asynchronously.
     * @param fileUrl The URL of the file to download.
     * @param outputFile The destination of the file.
     */
    public void startDownload(String fileUrl, File outputFile) {
        logger.info("Submitting download task for: {}", fileUrl);
        executor.submit(() -> performDownload(fileUrl, outputFile));
    }

    private void performDownload(String fileUrl, File outputFile) {
        DownloadInfo info = null;
        try {
            URL url = new URL(fileUrl);
            URLConnection connection = url.openConnection();
            long fileSize = connection.getContentLengthLong();
            String fileName = url.getFile().substring(url.getFile().lastIndexOf('/') + 1);
            
            // Initialize Info and Report Start
            info = new DownloadInfo(fileName, fileSize, fileUrl);
            activeDownloads.put(fileUrl, info);
            handleStart(info);

            // Start input stream. Will thread block.
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

                // Final Progress Update (Optional, but good practice)
                for (DownloadListener listener : listeners) {
                    listener.onDownloadProgress(info);
                }

                // Notify Listeners (Replaces handleComplete)
                for (DownloadListener listener : listeners) {
                    listener.onDownloadComplete(info, outputFile);
                }

            } catch (IOException e) {
                for (DownloadListener listener : listeners) {
                    listener.onDownloadError(info, e);
                }
            }

        } catch (Exception e) {
            // URL/Connection setup error
            if (info == null) {
                info = new DownloadInfo("Unknown", -1, fileUrl);
                activeDownloads.put(fileUrl, info);
            }
            for (DownloadListener listener : listeners) {
                listener.onDownloadError(info, e);
            }
        }
    }

    public long getRemoteFileSize(String fileUrl) {
        try {
            URL url = new URL(fileUrl);
            URLConnection connection = url.openConnection();
            if (connection instanceof HttpURLConnection) {
                ((HttpURLConnection) connection).setRequestMethod("HEAD");
            }
            connection.connect();

            return connection.getContentLengthLong();
        } catch (Exception e) {
            logger.error("Error fetching remote file size", e);
            return -1;
        }
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
    }

    private void handleProgress(DownloadInfo info) {
        int percent = (int) (info.getDownloadProgress() * 100);
        logger.debug("...Progress: {}% ({} / {} bytes)", percent, info.getDownloadedBytes(), info.getTotalFileSize());
    }

    private void handleComplete(DownloadInfo info, File outputFile) {
        logger.info("Download Complete! {}", info.getFileName());
        logger.info("File saved to: {}", outputFile);
    }

    private void handleError(DownloadInfo info, Exception e) {
        logger.error("Download Failed for {}", info.getFileName(), e);
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
        executor.shutdown();
        activeDownloads.clear();
        logger.info("FileDownloader service shut down.");
    }
}