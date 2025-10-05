package me.piitex.engine.fxloader;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Entry point for RenEngine.
 * <p>
 *     <pre>
 *         {@code
 *             public class App extends FXLoad {
 *
 *                 public void preInitialization() {
 *                     // Code to be run before gui is loaded
 *                 }
 *
 *                 public void initialization() {
 *                     // Build and render GUI.
 *                 }
 *             }
 *         }
 *     </pre>
 * </p>
 *
 */
public abstract class FXLoad extends Application {

    public FXLoad() {
        preInitialization();
    }

    @Override
    public void start(Stage primaryStage) {
        initialization(primaryStage);
    }

    public abstract void preInitialization();

    public abstract void initialization(Stage stage);
}
