package me.piitex.engine.loaders;

import javafx.scene.image.Image;
import javafx.scene.image.PixelBuffer;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;
import me.piitex.engine.LimitedHashMap;
import org.girod.javafx.svgimage.SVGImage;
import org.girod.javafx.svgimage.SVGLoader;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.Map;

/**
 * Used for loading images. When loading an image insert it inside the cache.
 */
public class ImageLoader {
    private final File file;

    public static final Map<String, Image> imageCache = new LimitedHashMap<>(50);
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

    public Image build() {
        if (useCache && imageCache.containsKey(file.getPath())) {
            return imageCache.get(file.getPath());
        }
        try {
            if (file.getName().endsWith(".svg")) {
                SVGImage svgImage = SVGLoader.load(file);
                return svgImage.toImage();
            }
            BufferedImage bufferedImage = ImageIO.read(file);
            Image image = getImage(bufferedImage);
            imageCache.put(file.getPath(), image);
            return image;
        } catch (IOException e) {
            return buildRaw();
        }
    }

    public Image buildRaw() {
        try {
            return new Image(new FileInputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public File getFile() {
        return file;
    }


    // Credit: https://stackoverflow.com/questions/30970005/bufferedimage-to-javafx-image
    private Image getImage(BufferedImage img) {
        //converting to a good type, read about types here: https://openjfx.io/javadoc/13/javafx.graphics/javafx/scene/image/PixelBuffer.html
        BufferedImage newImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB_PRE);
        newImg.createGraphics().drawImage(img, 0, 0, img.getWidth(), img.getHeight(), null);

        //converting the BufferedImage to an IntBuffer
        int[] type_int_agrb = ((DataBufferInt) newImg.getRaster().getDataBuffer()).getData();
        IntBuffer buffer = IntBuffer.wrap(type_int_agrb);

        //converting the IntBuffer to an Image, read more about it here: https://openjfx.io/javadoc/13/javafx.graphics/javafx/scene/image/PixelBuffer.html
        PixelFormat<IntBuffer> pixelFormat = PixelFormat.getIntArgbPreInstance();
        PixelBuffer<IntBuffer> pixelBuffer = new PixelBuffer(newImg.getWidth(), newImg.getHeight(), buffer, pixelFormat);
        return new WritableImage(pixelBuffer);
    }
}
