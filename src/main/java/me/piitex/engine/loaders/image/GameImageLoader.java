package me.piitex.engine.loaders.image;

import java.io.File;

/**
 * Uses running game path for easy image loading.
 */
public class GameImageLoader extends ImageLoader {

    /**
     * Loads an image via a filename from the base directory.
     * @param name Name of the image file.
     */
    public GameImageLoader(String name) {
        File directory = new File(System.getProperty("user.dir") + "/game/images/");
        super(new File(directory, name));
    }
}
