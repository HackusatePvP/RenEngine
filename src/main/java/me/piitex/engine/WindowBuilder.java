package me.piitex.engine;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.StageStyle;
import me.piitex.engine.loaders.image.ImageLoader;

/**
 * A builder class for constructing Window objects with various configuration options.
 * This provides a more intuitive and flexible way to create {@link Window} instances
 * compared to using multiple constructors with varying parameters.
 * <pre>
 * {@code
 * Window window = new WindowBuilder("My Game")
 * .setStageStyle(StageStyle.UNDECORATED)
 * .setRoot(new Pane())
 * .setDimensions(1280, 720)
 * .setBackgroundColor(Color.DARKBLUE)
 * .setFullscreen(true)
 * .build();
 * }
 * </pre>
 */
public class WindowBuilder {
    private final String title;
    private StageStyle stageStyle = StageStyle.DECORATED;
    private Pane root = new Pane();
    private ImageLoader icon;
    private double width = 1920;
    private double height = 1080;
    private Color backgroundColor = Color.BLACK;
    private boolean fullscreen = false;
    private boolean maximized = false;
    private boolean focused = true;
    private boolean scale = true;
    private boolean antialiasing = true;

    /**
     * Starts the building process for a new Window with a required title.
     *
     * @param title The process title and visible label of the window.
     */
    public WindowBuilder(String title) {
        this.title = title;
    }

    /**
     * Sets the style of the window.
     * @param stageStyle The {@link StageStyle} for the window (e.g., DECORATED, UNDECORATED).
     * @return The current WindowBuilder instance for method chaining.
     */
    public WindowBuilder setStageStyle(StageStyle stageStyle) {
        this.stageStyle = stageStyle;
        return this;
    }

    /**
     * Sets the scene root pane. This is the underlying base pane for the JavaFX scene.
     * @param root The explicit {@link Pane} type to be used as the root.
     * @return The current WindowBuilder instance for method chaining.
     */
    public WindowBuilder setRoot(Pane root) {
        this.root = root;
        return this;
    }

    /**
     * Sets the window's taskbar and title bar icon.
     * @param icon An {@link ImageLoader} instance mapping to the targeted window icon.
     * @return The current WindowBuilder instance for method chaining.
     */
    public WindowBuilder setIcon(ImageLoader icon) {
        this.icon = icon;
        return this;
    }

    /**
     * Sets the preferred width and height of the window upon initial rendering.
     * @param width The target width in pixels.
     * @param height The target height in pixels.
     * @return The current WindowBuilder instance for method chaining.
     */
    public WindowBuilder setDimensions(double width, double height) {
        this.width = width;
        this.height = height;
        return this;
    }

    /**
     * Sets the background color fill of the window's root pane and underlying scene.
     * @param backgroundColor The JavaFX {@link Color} for the window's background.
     * @return The current WindowBuilder instance for method chaining.
     */
    public WindowBuilder setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    /**
     * Sets whether the window should launch and maintain a borderless fullscreen mode.
     * @param fullscreen True to enable fullscreen, false otherwise.
     * @return The current WindowBuilder instance for method chaining.
     */
    public WindowBuilder setFullscreen(boolean fullscreen) {
        this.fullscreen = fullscreen;
        return this;
    }

    /**
     * Sets whether the window should launch already maximized across the user's primary display.
     * @param maximized True to start maximized, false otherwise.
     * @return The current WindowBuilder instance for method chaining.
     */
    public WindowBuilder setMaximized(boolean maximized) {
        this.maximized = maximized;
        return this;
    }

    /**
     * Sets whether the window will explicitly request OS focus to be brought to the front on launch.
     * @param focused True to aggressively request focus, false otherwise.
     * @return The current WindowBuilder instance for method chaining.
     */
    public WindowBuilder setFocused(boolean focused) {
        this.focused = focused;
        return this;
    }

    /**
     * Dictates whether the window's content automatically uses affine transformations
     * to scale logically when the user resizes the window bounds.
     * @param scale True to auto-scale components, false to handle resizing manually.
     * @return The current WindowBuilder instance for method chaining.
     */
    public WindowBuilder setScale(boolean scale) {
        this.scale = scale;
        return this;
    }

    /**
     * Toggles default JavaFX text and node anti-aliasing logic on or off.
     * Turning this off can be useful for pixel-art style desktop applications.
     * @param aliasing True to retain smoothing, false to enforce hard pixel edges.
     * @return The current WindowBuilder instance for method chaining.
     */
    public WindowBuilder setAntiAliasing(boolean aliasing) {
        this.antialiasing = aliasing;
        return this;
    }

    /**
     * Constructs and returns a fully initialized {@link Window} object based on the builder's stored configurations.
     * @return A new runtime-ready Window instance.
     */
    public Window build() {
        return new Window(this); // Calls the package-protected/public constructor in the Window class
    }

    /**
     * @return The configured window title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return The configured stage style.
     */
    public StageStyle getStageStyle() {
        return stageStyle;
    }

    /**
     * @return The configured root pane instance.
     */
    public Pane getRoot() {
        return root;
    }

    /**
     * @return The configured image loader logic for the window icon.
     */
    public ImageLoader getIcon() {
        return icon;
    }

    /**
     * @return The configured launch width.
     */
    public double getWidth() {
        return width;
    }

    /**
     * @return The configured launch height.
     */
    public double getHeight() {
        return height;
    }

    /**
     * @return The configured background color fill.
     */
    public Color getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * @return True if fullscreen is enabled, false otherwise.
     */
    public boolean isFullscreen() {
        return fullscreen;
    }

    /**
     * @return True if starting maximized, false otherwise.
     */
    public boolean isMaximized() {
        return maximized;
    }

    /**
     * @return True if the window should steal focus upon loading.
     */
    public boolean isFocused() {
        return focused;
    }

    /**
     * @return True if affine scaling transformations are enabled.
     */
    public boolean isScale() {
        return scale;
    }

    /**
     * @return True if JavaFX text and node anti-aliasing should be retained.
     */
    public boolean isAntialiasing() {
        return antialiasing;
    }
}