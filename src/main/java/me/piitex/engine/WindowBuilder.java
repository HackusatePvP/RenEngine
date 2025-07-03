package me.piitex.engine;

import javafx.scene.paint.Color;
import javafx.stage.StageStyle;
import me.piitex.engine.loaders.ImageLoader;

/**
 * A builder class for constructing Window objects with various configuration options.
 * This provides a more intuitive and flexible way to create Window instances
 * compared to using multiple constructors.
 * <pre>
 * {@code
 * Window window = new WindowBuild("My Game")
 * .setStageStyle(StageStyle.UNDECORATED)
 * .setDimensions(1280, 720)
 * .setBackgroundColor(Color.DARKBLUE)
 * .setCaptureInput(true)
 * .setFullscreen(true)
 * .build();
 * }
 * </pre>
 */
public class WindowBuilder {
    private final String title;
    private StageStyle stageStyle = StageStyle.DECORATED;
    private ImageLoader icon;
    private int width = 1920;
    private int height = 1080;
    private Color backgroundColor = Color.BLACK;
    private boolean captureInput = true;
    private boolean fullscreen = false;
    private boolean maximized = false;
    private boolean focused = true;
    private boolean scale = true;

    /**
     * Starts the building process for a new Window with a required title.
     *
     * @param title The title of the window.
     */
    public WindowBuilder(String title) {
        this.title = title;
    }

    /**
     * Sets the style of the window.
     * @param stageStyle The {@link StageStyle} for the window (e.g., DECORATED, UNDECORATED).
     * @return The current WindowBuild instance for chaining.
     */
    public WindowBuilder setStageStyle(StageStyle stageStyle) {
        this.stageStyle = stageStyle;
        return this;
    }

    /**
     * Sets the icon for the window.
     * @param icon An {@link ImageLoader} for the window's icon.
     * @return The current WindowBuild instance for chaining.
     */
    public WindowBuilder setIcon(ImageLoader icon) {
        this.icon = icon;
        return this;
    }

    /**
     * Sets the preferred width and height of the window.
     * @param width The width of the window.
     * @param height The height of the window.
     * @return The current WindowBuild instance for chaining.
     */
    public WindowBuilder setDimensions(int width, int height) {
        this.width = width;
        this.height = height;
        return this;
    }

    /**
     * Sets the background color of the window's root pane.
     * @param backgroundColor The {@link Color} for the window's background.
     * @return The current WindowBuild instance for chaining.
     */
    public WindowBuilder setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    /**
     * Determines if the window should capture user input (e.g., mouse clicks, key presses).
     * @param captureInput True to capture input, false otherwise.
     * @return The current WindowBuild instance for chaining.
     */
    public WindowBuilder setCaptureInput(boolean captureInput) {
        this.captureInput = captureInput;
        return this;
    }

    /**
     * Sets whether the window should be in fullscreen mode.
     * @param fullscreen True for fullscreen, false otherwise.
     * @return The current WindowBuild instance for chaining.
     */
    public WindowBuilder setFullscreen(boolean fullscreen) {
        this.fullscreen = fullscreen;
        return this;
    }

    /**
     * Sets whether the window should be maximized on launch.
     * @param maximized True to maximize, false otherwise.
     * @return The current WindowBuild instance for chaining.
     */
    public WindowBuilder setMaximized(boolean maximized) {
        this.maximized = maximized;
        return this;
    }

    /**
     * Sets whether the window should be focused on launch.
     * @param focused True to focus, false otherwise.
     * @return The current WindowBuild instance for chaining.
     */
    public WindowBuilder setFocused(boolean focused) {
        this.focused = focused;
        return this;
    }

    public WindowBuilder setScale(boolean scale) {
        this.scale = scale;
        return this;
    }

    /**
     * Constructs and returns a new {@link Window} object based on the builder's configurations.
     * @return A new Window instance.
     */
    public Window build() {
        return new Window(this); // Calls the private constructor in the Window class
    }

    // Getters for the Window class to access these properties
    public String getTitle() { return title; }
    public StageStyle getStageStyle() { return stageStyle; }
    public ImageLoader getIcon() { return icon; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public Color getBackgroundColor() { return backgroundColor; }
    public boolean isCaptureInput() { return captureInput; }
    public boolean isFullscreen() { return fullscreen; }
    public boolean isMaximized() { return maximized; }
    public boolean isFocused() { return focused; }

    public boolean isScale() {
        return scale;
    }
}
