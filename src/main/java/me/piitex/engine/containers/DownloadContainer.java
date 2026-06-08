package me.piitex.engine.containers;

import com.google.common.util.concurrent.AtomicDouble;
import javafx.application.Platform;
import javafx.geometry.Pos;
import me.piitex.engine.layouts.VerticalLayout;
import me.piitex.engine.overlays.ProgressBarOverlay;
import me.piitex.engine.overlays.TextOverlay;
import me.piitex.os.DownloadInfo;
import me.piitex.os.DownloadListener;
import me.piitex.os.FileDownloader;

import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class DownloadContainer extends EmptyContainer {
    private final String label;
    private String url;
    private File output;
    private final FileDownloader downloader;
    private DownloadInfo downloadInfo;

    private VerticalLayout main;
    private TextOverlay message, downloadText, downloadSpeed;
    private ProgressBarOverlay downloadProgress;

    private Consumer<DownloadInfo> downloadComplete;
    private Consumer<DownloadInfo> downloadError;
    private Consumer<DownloadInfo> downloadCancelled;

    private final AtomicLong lastDownloadBytes = new AtomicLong();
    private final AtomicLong lastDownloadTime = new AtomicLong();
    private final AtomicReference<Double> smoothedSpeedRef = new AtomicReference<>();


    public DownloadContainer(double width, double height, String label, String url, File output) {
        super(0, 0, width, height);
        this.label = label;
        this.url = url;
        this.output = output;
        this.downloader = new FileDownloader();
        init();
    }

    public DownloadContainer(double width, double height, String label, DownloadInfo downloadInfo, FileDownloader downloader) {
        super(0, 0, width, height);
        this.label = label;
        this.downloadInfo = downloadInfo;
        this.downloader = downloader;
        init();
    }

    public Consumer<DownloadInfo> getOnDownloadComplete() {
        return downloadComplete;
    }

    public void onDownloadComplete(Consumer<DownloadInfo> downloadComplete) {
        this.downloadComplete = downloadComplete;
    }

    public Consumer<DownloadInfo> getDownloadError() {
        return downloadError;
    }

    public void onDownloadError(Consumer<DownloadInfo> downloadError) {
        this.downloadError = downloadError;
    }

    public Consumer<DownloadInfo> getDownloadCancelled() {
        return downloadCancelled;
    }

    public void onDownloadCancelled(Consumer<DownloadInfo> downloadCancelled) {
        this.downloadCancelled = downloadCancelled;
    }

    private void init() {
        main = new VerticalLayout(getWidth(), getHeight());
        main.setMaxSize(main.getWidth(), main.getHeight());
        main.setAlignment(Pos.TOP_CENTER);
        main.setY(20);
        addElement(main);

        message = new TextOverlay(label);
        main.addElement(message);

        downloadProgress = new ProgressBarOverlay();
        main.addElement(downloadProgress);

        if (downloader.getListeners().isEmpty()) {
            hookDownloadListeners();
        }

        downloadText = new TextOverlay("0/0");
        main.addElement(downloadText);

        downloadSpeed = new TextOverlay("0 MB/s");
        main.addElement(downloadSpeed);
    }

    public void startDownload() {
        if (url != null && output != null) {
            downloader.startDownload(url, output);
        } else if (downloadInfo != null) {
            downloader.startDownload(downloadInfo.getDownloadUrl(), downloadInfo.getOutput());
        } else {
            throw new RuntimeException("Could not initialize download. URL or output not specified.");
        }
    }

    private void hookDownloadListeners() {
        smoothedSpeedRef.set(0d);

        downloader.addDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(DownloadInfo info) {
                Platform.runLater(() -> {
                    downloadProgress.getProgressBar().progressProperty().set(0);
                });
            }

            @Override
            public void onDownloadProgress(DownloadInfo info) {
                Platform.runLater(() -> {
                    downloadProgress.getProgressBar().progressProperty().set(info.getDownloadProgress());
                    if (info.getTotalFileSize() < 900000) { // Use Kb
                        downloadText.setText(info.getDownloadedBytes() / 1024 + "/" + info.getTotalFileSize() / 1024 + "KiB");
                    } else if (info.getTotalFileSize() < 900000000) { // Use MB
                        downloadText.setText(info.getDownloadedBytes() / 1000000 + "/" + info.getTotalFileSize() / 1000000 + "MB");
                    } else { // Use GB
                        downloadText.setText(info.getDownloadedBytes() / 1000000000 + "/" + info.getTotalFileSize() / 1000000000 + "GB");
                    }

                    long currentTime = System.currentTimeMillis();

                    long bytesDownloadedSinceLastUpdate = info.getDownloadedBytes() - lastDownloadBytes.get();
                    long timeElapsedSinceLastUpdate = currentTime - lastDownloadTime.get();

                    double instantaneousBytesPerSecond = 0.0;
                    if (timeElapsedSinceLastUpdate > 0 && bytesDownloadedSinceLastUpdate > 0) {
                        instantaneousBytesPerSecond = (double) bytesDownloadedSinceLastUpdate / (timeElapsedSinceLastUpdate / 1000.0);
                    }

                    final double SMOOTHING_FACTOR = 0.15; // 0.15 is responsive but stable

                    double previousSmoothedSpeed = smoothedSpeedRef.get();
                    double newSmoothedSpeed = (instantaneousBytesPerSecond * SMOOTHING_FACTOR) +
                            (previousSmoothedSpeed * (1.0 - SMOOTHING_FACTOR));

                    // Update the reference for the next calculation
                    smoothedSpeedRef.set(newSmoothedSpeed);

                    // Update the 'last' values for the next calculation
                    lastDownloadBytes.set(info.getDownloadedBytes());
                    lastDownloadTime.set(currentTime);

                    double speed = (newSmoothedSpeed / (1024 * 1024));
                    String speedText = String.format("%.2f", speed);
                    downloadSpeed.setText(speedText + " Mb/s");
                });
            }

            @Override
            public void onDownloadComplete(DownloadInfo info, File outputFile) {
                if (downloadComplete != null) {
                    downloadComplete.accept(info);
                } else {
                    throw new RuntimeException("Download completion is not handled!");
                }
            }

            @Override
            public void onDownloadError(DownloadInfo info, Exception e) {
                if (downloadError != null) {
                    downloadError.accept(info);
                } else {
                    throw new RuntimeException("Download error is not handled!");
                }
            }

            @Override
            public void onDownloadCancel(DownloadInfo info) {
                if (downloadCancelled != null) {
                    downloadCancelled.accept(info);
                } else {
                    throw new RuntimeException("Download cancelled is not handled!");
                }
            }
        });
    }

    public FileDownloader getDownloader() {
        return downloader;
    }

    public VerticalLayout getMain() {
        return main;
    }

    public TextOverlay getMessage() {
        return message;
    }

    public ProgressBarOverlay getDownloadProgress() {
        return downloadProgress;
    }

    public TextOverlay getDownloadText() {
        return downloadText;
    }
}
