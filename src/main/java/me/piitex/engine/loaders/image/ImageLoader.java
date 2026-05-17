package me.piitex.engine.loaders.image;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import me.piitex.engine.maps.LimitedHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;

/**
 * Loads an image from a file path and assembles the JavaFX {@link Image} using {@link #build()}.
 * The image will be added to a global cache on the first load.
 * Cached images provide better performance as they skip IO operations.
 */
public abstract class ImageLoader {
    private final File file;
    private double width, height;
    private boolean preserveRatio = false;
    private boolean smoothing = false;

    public static final Map<String, Image> imageCache = new LimitedHashMap<>(50);
    private static final Map<String, Long> imageSizeCache = new LimitedHashMap<>(50);
    private static final Logger logger = LoggerFactory.getLogger(ImageLoader.class);

    public static boolean useCache = true;

    protected ImageLoader(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public void setSize(double width, double height) {
        this.width = width;
        this.height = height;
    }

    public void setPreserveRatio(boolean preserveRatio) {
        this.preserveRatio = preserveRatio;
    }

    public void setSmoothing(boolean smoothing) {
        this.smoothing = smoothing;
    }

    public Image build() {
        if (useCache && imageCache.containsKey(file.getPath()) && (file.length() == imageSizeCache.get(file.getPath()))) {
            return imageCache.get(file.getPath());
        }

        try (FileInputStream fis = new FileInputStream(file)) {
            Image image = new Image(fis, width, height, preserveRatio, smoothing);
            if (useCache) {
                imageCache.put(file.getPath(), image);
                imageSizeCache.put(file.getPath(), file.length());
            }
            return image;
        } catch (FileNotFoundException e) {
            logger.error("Could not find image '{}'", file.getAbsolutePath(), e);
            return null;
        } catch (Exception e) {
            logger.error("Error loading image '{}'", file.getAbsolutePath(), e);
            return null;
        }
    }

    // Credit: https://stackoverflow.com/questions/30970005/bufferedimage-to-javafx-image
    private Image getImage(BufferedImage img) {
        int width = (int) getWidth();
        int height = (int) getHeight();

        if (width <= 0) {
            width = img.getWidth();
        }
        if (height <= 0) {
            height = img.getHeight();
        }

        return SwingFXUtils.toFXImage(img, new WritableImage(width, height));
    }

    public static void clearCache() {
        imageCache.clear();
        imageSizeCache.clear();
    }
}
