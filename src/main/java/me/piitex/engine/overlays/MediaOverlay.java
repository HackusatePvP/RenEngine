package me.piitex.engine.overlays;

import javafx.scene.Node;

import java.io.File;

/**
 * Video scenes are not fully implemented yet and there are severe limitations.
 * Most video encoders are not supported and even if they are they most likely do not work on other operating systems.
 * If your video is rendering a black screen that means the video encoder is not supported by the os. H.264 is the more supported encoding but is superseded by HEVC.
 * HEVC is not supported by Windows by default.
 * <pre>
 * Supported types that may work are
 *     1. H.264 AVC (.mp4)
 *     2. H.265 HEVC (.mp4)
 *     3. VP6 (.vp6, .zzz)
 * </pre>
 * <p>
 * Make sure the video resolution is set the resolution of the game. If your game is configured to be 1080p then the video must also be 1080p.
 * RenJava can also auto-size the video to match the window resolution.
 */
public class MediaOverlay extends Overlay implements Region {
    private final String filePath;
    private double width = -1, height = -1;
    private double scaleWidth, scaleHeight;
    private boolean loop = false;

    public MediaOverlay(String filePath, int x, int y) {
        this.filePath = filePath;
        setX(x);
        setY(y);
        File file = new File(System.getProperty("user.dir") + "/game/" + filePath);
        if (!file.exists()) {
            // ERROR
            return;
        }
    }

    public MediaOverlay(String filePath, int x, int y, int width, int height) {
        this.filePath = filePath;
        setX(x);
        setY(y);
        this.width = width;
        this.height = height;
        File file = new File(System.getProperty("user.dir") + "/game/" + filePath);
        if (!file.exists()) {
            // ERROR
            return;
        }
    }

    public String getFilePath() {
        return filePath;
    }

    public boolean isLoop() {
        return loop;
    }

    public void setLoop(boolean loop) {
        this.loop = loop;

    }

    public void play() {

    }

    public void stop() {

    }

    @Override
    public Node render() {
//        MediaView mediaView = new MediaView(RenJava.PLAYER.getCurrentMedia());
//        if (width != -1) {
//            mediaView.setFitWidth(width);
//        }
//        if (height != -1) {
//            mediaView.setFitHeight(height);
//        }
//        RenLogger.LOGGER.info("Playing media '{}'", System.getProperty("user.dir") + "/" + filePath);
//        RenJava.PLAYER.getCurrentMedia().play();
//        return mediaView;
        return null;
    }

    @Override
    public double getWidth() {
        return width;
    }

    @Override
    public double getHeight() {
        return height;
    }

    @Override
    public void setWidth(double w) {
        this.width = w;
    }

    @Override
    public void setHeight(double h) {
        this.height = h;
    }

    @Override
    public double getScaleWidth() {
        return scaleWidth;
    }

    @Override
    public void setScaleWidth(double w) {
        this.scaleWidth = w;
    }

    @Override
    public double getScaleHeight() {
        return scaleHeight;
    }

    @Override
    public void setScaleHeight(double h) {
        this.scaleHeight = h;
    }
}
