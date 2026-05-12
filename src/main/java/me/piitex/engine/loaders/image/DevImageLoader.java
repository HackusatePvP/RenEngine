package me.piitex.engine.loaders.image;

import java.io.File;

/**
 * Same pathing as {@link GameImageLoader}. Allows a static testing directory to make development easier and more convenient.
 * The directory needs to be initialized before JavaFX.
 * <pre>
 *     {@code
 *
 * public class Game extends FXLoad {
 *
 *     @Override
 *     public void preInitialization() {
 *         // Initiate testing directory (remove later)
 *         DevImageLoader.setTestingDirectory(new File("/path/to/game/"));
 *     }
 *
 *     @Override
 *     public void initialization(Stage s) {
 *         // Create main window
 *     }
 * }
 *
 *     }
 * </pre>
 */
public class DevImageLoader extends ImageLoader {
    private static File testingDirectory;

    public DevImageLoader(String image) {
        super(new File(testingDirectory, image));
    }

    public static void setTestingDirectory(File testingDirectory) {
        DevImageLoader.testingDirectory = testingDirectory;
    }
}
