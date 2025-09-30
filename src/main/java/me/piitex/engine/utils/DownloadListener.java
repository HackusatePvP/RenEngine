package me.piitex.engine.utils;

import java.io.File;

public interface DownloadListener {

    /**
     * Called when the download successfully begins.
     * @param info The initial state of the download.
     */
    void onDownloadStart(DownloadInfo info);

    /**
     * Called periodically during the download with progress updates.
     * @param info The current state of the download.
     */
    void onDownloadProgress(DownloadInfo info);

    /**
     * Called when the download task completes successfully.
     * @param info The final state of the download.
     * @param outputFile The file location where the data was saved.
     */
    void onDownloadComplete(DownloadInfo info, File outputFile);

    /**
     * Called when the download encounters a failure.
     * @param info The state of the download at the time of the error.
     * @param e The exception that caused the failure.
     */
    void onDownloadError(DownloadInfo info, Exception e);

    /**
     * Called when the download is explicitly cancelled by the user via cancelDownload(url).
     * @param info The state of the download at the time of cancellation.
     */
    void onDownloadCancel(DownloadInfo info);
}