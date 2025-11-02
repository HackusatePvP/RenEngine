package me.piitex.engine.loaders;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import me.piitex.engine.LimitedHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;

/**
 * Used for loading images. When loading an image insert it inside the cache.
 */
public class ImageLoader {
    private final File file;
    private double width, height;

    public static final Map<String, Image> imageCache = new LimitedHashMap<>(50);
    private static final Map<String, Double> imageSizeCache = new LimitedHashMap<>(50);
    private static final Logger logger = LoggerFactory.getLogger(ImageLoader.class);

    public static boolean useCache = true;

    /**
     * Loads an image via a filename from the base directory.
     * @param name Name of the image file.
     */
    public ImageLoader(String name) {
        File directory = new File(System.getProperty("user.dir") + "/game/images/");
        this.file = new File(directory, name);
    }

    public ImageLoader(String directory, String name) {
        File fileDirectory = new File(System.getProperty("user.dir") + "/" + directory + "/");
        this.file = new File(fileDirectory, name);
    }

    public ImageLoader(File file) {
        this.file = file;
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


    public Image build() {
        if (useCache && imageCache.containsKey(file.getPath()) && (width + height == imageSizeCache.get(file.getPath()))) {
            return imageCache.get(file.getPath());
        }

        try {
            Image image = new Image(new FileInputStream(file), width, height, false, false);
            if (useCache) {
                imageCache.put(file.getPath(), image);
                imageSizeCache.put(file.getPath(), width + height);
            }
            return image;
        } catch (FileNotFoundException e) {
            logger.error("Could not find image '{}'", file.getAbsolutePath(), e);
            return null;
        }
    }

    public File getFile() {
        return file;
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
}
