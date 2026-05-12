package me.piitex.engine.loaders.image;

import java.io.File;

public class BaseImageLoader extends ImageLoader {

    /**
     * Loads an image via a filename from the base directory.
     * @param name Name of the image file.
     */
    public BaseImageLoader(String name) {
        super(new File(name));
    }

    public BaseImageLoader(String directory, String name) {
        super(new File(directory, name));
    }

    public BaseImageLoader(File directory, String name) {
        super(new File(directory, name));
    }

    public BaseImageLoader(File file) {
        super(file);
    }
}
