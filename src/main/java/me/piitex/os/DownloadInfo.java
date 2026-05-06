package me.piitex.os;

import java.io.File;

public class DownloadInfo {
    private final String fileName;
    private final File output;
    private final long totalFileSize; // Total size in bytes
    private long downloadedBytes;
    private double downloadProgress; // Between 0.0 and 1.0
    private final String downloadUrl;
    private boolean isComplete;

    public DownloadInfo(String fileName, File output, long totalFileSize, String downloadUrl) {
        this.fileName = fileName;
        this.output = output;
        this.totalFileSize = totalFileSize;
        this.downloadUrl = downloadUrl;
        this.downloadedBytes = 0;
        this.downloadProgress = 0.0;
        this.isComplete = false;
    }

    public String getFileName() {
        return fileName;
    }

    public File getOutput() {
        return output;
    }

    public long getTotalFileSize() {
        return totalFileSize;
    }

    public long getDownloadedBytes() {
        return downloadedBytes;
    }

    public double getDownloadProgress() {
        return downloadProgress;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public boolean isComplete() { return
            isComplete;
    }

    public void setDownloadedBytes(long downloadedBytes) {
        this.downloadedBytes = downloadedBytes;
        if (totalFileSize > 0) {
            this.downloadProgress = (double) downloadedBytes / totalFileSize;
        } else {
            this.downloadProgress = 0.0;
        }
    }
    
    public void setComplete(boolean complete) {
        isComplete = complete;
    }
}