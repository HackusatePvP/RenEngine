package me.piitex.os;

public class DownloadInfo {
    private final String fileName;
    private final long totalFileSize; // Total size in bytes
    private long downloadedBytes;
    private double downloadProgress; // Between 0.0 and 1.0
    private String downloadUrl;
    private boolean isComplete;

    public DownloadInfo(String fileName, long totalFileSize, String downloadUrl) {
        this.fileName = fileName;
        this.totalFileSize = totalFileSize;
        this.downloadUrl = downloadUrl;
        this.downloadedBytes = 0;
        this.downloadProgress = 0.0;
        this.isComplete = false;
    }

    // Getters
    public String getFileName() { return fileName; }
    public long getTotalFileSize() { return totalFileSize; }
    public long getDownloadedBytes() { return downloadedBytes; }
    public double getDownloadProgress() { return downloadProgress; }
    public String getDownloadUrl() { return downloadUrl; }
    public boolean isComplete() { return isComplete; }

    // Setters (used by the FileDownloader)
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