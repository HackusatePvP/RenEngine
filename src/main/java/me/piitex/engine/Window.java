package me.piitex.engine;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import me.piitex.engine.containers.Container;
import me.piitex.engine.containers.EmptyContainer;
import me.piitex.engine.hanlders.IWindowResize;
import me.piitex.engine.hanlders.events.WindowResizeEvent;
import me.piitex.engine.layouts.Layout;
import me.piitex.engine.loaders.image.ImageLoader;
import me.piitex.engine.overlays.*;
import me.piitex.os.OSUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

/**
 * The Window serves as the primary GUI component, managing the rendering process for the engine.
 * Instead of directly tracking arbitrary elements, the Window strictly houses and manages
 * top-level {@link Container} objects.
 *
 * <p>Multiple windows can be created and rendered simultaneously. The window's title serves as its process name and label.
 * The stage style dictates the window's appearance, with {@link StageStyle#DECORATED} providing standard
 * window controls (close, minimize, maximize) and {@link StageStyle#UNDECORATED} removing the title bar
 * for a borderless experience, often used in full-screen applications.</p>
 *
 * <pre>{@code
 * Window window = new WindowBuilder("My Application Window")
 * .setStageStyle(StageStyle.UNDECORATED)
 * .setDimensions(800, 600)
 * .setBackgroundColor(Color.BLACK)
 * .build();
 * }</pre>
 *
 * <p>
 * To display UI elements within a window, a {@link Container} must first be created and populated.
 * Then, that container is added directly to the window's managed collection.</p>
 * <pre>{@code
 * Window window = application.getWindow();
 * Container container = new EmptyContainer(x, y, width, height);
 * window.addContainer(container);
 * }</pre>
 *
 * <p>
 * All GUI-related functions, especially those involving scene graph modifications,
 * must be executed on the JavaFX Application Thread.</p> This example uses a native {@link Thread} for simplicity.
 * <pre>{@code
 * new Thread( () -> {
 * // Code to be executed asynchronously
 * loadBackend();
 *
 * Platform.runLater( () -> {
 * // Any gui related code.
 * initializeProgressIndicator();
 * })
 * });
 * }</pre>
 *
 * @see Container
 * @see Overlay
 * @see Layout
 * @see Platform#runLater(Runnable)
 */
public class Window {
    private final String title;
    private final ImageLoader icon;
    private final StageStyle stageStyle;
    private double initialWidth, initialHeight;
    private double width, height;
    private double drawWidth, drawHeight;
    private boolean fullscreen, maximized ;
    private Color backgroundColor;
    private Stage stage;
    private Scene scene;
    private Pane root;

    private final boolean scale;
    private final boolean focused;
    private boolean antialiasing;

    private TreeMap<Integer, Container> containers = new TreeMap<>();
    private Container currentPopup = null;
    private IWindowResize windowResize;

    private static final Logger logger = LoggerFactory.getLogger(Window.class);

    // -------- OS Specific Values ----------
    // JavaFX does not take into account the window title bar height.
    // This causes alignment issues with various operating systems.
    private static final int LINUX_WINDOW_HEIGHT = 35;
    private static final int WIN_WINDOW_HEIGHT = 40;
    private static final int MAC_WINDOW_HEIGHT = 40;


    /**
     * Constructs a Window instance using properties defined in a {@link WindowBuilder}.
     * This allows for a flexible and readable way to configure window properties.
     *
     * <p>Example usage:</p>
     * <pre>{@code
     * WindowBuilder builder = new WindowBuilder()
     * .setStageStyle(StageStyle.UNDECORATED)
     * .setDimensions(800, 600)
     * .setBackgroundColor(Color.BLACK)
     * Window window = new Window(builder);
     *
     * // Add containers
     * window.addContainer(someContainer);
     * }</pre>
     * @param builder The {@link WindowBuilder} instance containing window configuration.
     */
    public Window(WindowBuilder builder) {
        this.title = builder.getTitle();
        this.stageStyle = builder.getStageStyle();
        this.root = builder.getRoot();
        this.icon = builder.getIcon();
        this.width = builder.getWidth();
        this.height = builder.getHeight();
        this.initialWidth = builder.getWidth();
        this.initialHeight = builder.getHeight();
        this.backgroundColor = builder.getBackgroundColor();
        this.fullscreen = builder.isFullscreen();
        this.maximized = builder.isMaximized();
        this.focused = builder.isFocused();
        this.scale = builder.isScale();
        this.antialiasing = builder.isAntialiasing();
        buildStage();

        // Display stage.
        render();
    }

    /**
     * Initializes the JavaFX Stage with the configured properties from the `WindowBuilder`.
     * This method applies the title, style, dimensions, icon, scaling behaviors,
     * anti-aliasing preferences, and initializes the root scene. Handles OS-specific
     * window height discrepancies internally.
     */
    protected void buildStage() {
        stage = new Stage();

        if (icon != null) {
            Image windowIcon = icon.build();
            if (windowIcon != null) {
                stage.getIcons().add(windowIcon);
            }
        }
        stage.setTitle(title);
        stage.initStyle(stageStyle);
        stage.setWidth(width);

        // Linux, Windows, and Mac handle sizing of windows differently.
        // With the top control bar enabled, the height will be off.

        if (OSUtil.getOS().toLowerCase().contains("linux")) {
            stage.setHeight(height);
            this.drawHeight = height - LINUX_WINDOW_HEIGHT;
        } else if (OSUtil.getOS().toLowerCase().contains("window")) {
            stage.setHeight(height);
            this.drawHeight = height - WIN_WINDOW_HEIGHT;
        } else if (OSUtil.getOS().toLowerCase().contains("mac")) {
            stage.setHeight(height);
            this.drawHeight = height - MAC_WINDOW_HEIGHT;
        } else {
            stage.setHeight(height);
        }
        this.drawWidth = width;

        stage.setMaximized(maximized);
        stage.setFullScreen(fullscreen);
        root.setPrefSize(width, height);
        root.setMaxSize(width, height);
        logger.info("Window Size: '{}', '{}'", width, height);
        logger.info("Draw Size ({}, {})", drawWidth, drawHeight);

        if (scale) {
            Scale scale = new Scale(getWidthScale(), getHeightScale(), 0, 0);
            root.getTransforms().setAll(scale);
        }

        if (!antialiasing) {
            logger.warn("Forced disabled anti-aliasing.");
            System.setProperty("prism.lcdtext", "false");
            System.setProperty("prism.subpixeltext", "false");
        }

        scene = new Scene(root);
        stage.setScene(scene);

        handleWindowScaling();

        if (backgroundColor != null) {
            updateBackground(backgroundColor);
        }
    }

    /**
     * Updates the background color of the window's root pane and scene.
     * @param color The new background color to apply.
     */
    public void updateBackground(Color color) {
        this.backgroundColor = color;
        root.setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
        stage.getScene().setFill(color);
    }

    /**
     * Retrieves the current background color of the window.
     * @return The current background color.
     */
    public Color getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * Retrieves the JavaFX Stage associated with this window.
     * @return The current Stage.
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * Retrieves the JavaFX Scene associated with this window.
     * @return The current Scene.
     */
    public Scene getScene() {
        return scene;
    }

    /**
     * Retrieves the root Pane of the window's scene graph.
     * @return The root Pane.
     */
    public Pane getRoot() {
        return root;
    }

    /**
     * Retrieves the currently configured width of the window.
     * @return The window width.
     */
    public double getWidth() {
        return width;
    }

    /**
     * Sets a new width for the window. Updates the initial width tracker and clears any existing
     * scaling transformations from the root pane.
     * @param width The new width in pixels.
     */
    public void setWidth(double width) {
        this.width = width;
        this.initialWidth = width;
        stage.setWidth(width);
        root.getTransforms().clear();
    }

    /**
     * Sets a new height for the window. Updates the initial height tracker and clears any existing
     * scaling transformations from the root pane.
     * @param height The new height in pixels.
     */
    public void setHeight(double height) {
        this.height = height;
        this.initialHeight = height;
        stage.setHeight(height);
        root.getTransforms().clear();
    }

    /**
     * Retrieves the currently configured height of the window.
     * @return The window height.
     */
    public double getHeight() {
        return height;
    }

    /**
     * Retrieves the adjusted width of the scene which accounts for the window title bar.
     * @return Width in pixels.
     */
    public double getDrawWidth() {
        return drawWidth;
    }

    /**
     * Retrieves the adjusted height of the scene which accounts for the window title bar.
     * @return Height in pixels.
     */
    public double getDrawHeight() {
        return drawHeight;
    }

    /**
     * Calculates the horizontal scale factor by dividing the current width by the initial width.
     * Useful for dynamic resizing and responsive UI adjustments.
     * @return The horizontal scaling multiplier.
     */
    public double getWidthScale() {
        return stage.getWidth() / initialWidth;
    }

    /**
     * Calculates the vertical scale factor by dividing the current height by the initial height.
     * Useful for dynamic resizing and responsive UI adjustments.
     * @return The vertical scaling multiplier.
     */
    public double getHeightScale() {
        return stage.getHeight() / initialHeight;
    }

    /**
     * Toggles the window between full-screen and windowed modes.
     * Reverts to the explicitly set width and height if exiting full-screen mode.
     * @param fullscreen True to set to full-screen, false for windowed.
     */
    public void setFullscreen(boolean fullscreen) {
        this.fullscreen = fullscreen;
        if (stage != null) {
            stage.setFullScreen(fullscreen);
            if (!fullscreen) {
                stage.setWidth(width);
                stage.setHeight(height);
            }
        }
    }

    /**
     * Toggles the window between maximized and normal states.
     * Reverts to the explicitly set width and height if exiting the maximized state.
     * @param maximized True to maximize the window, false for normal size.
     */
    public void setMaximized(boolean maximized) {
        this.maximized = maximized;
        if (stage != null) {
            stage.setMaximized(maximized);
            if (!maximized) {
                stage.setWidth(width);
                stage.setHeight(height);
            }
        }
    }

    /**
     * Adds a {@link Container} to the window using its intrinsically defined index.
     * @param container The container to add.
     */
    public void addContainer(Container container) {
        addContainer(container, container.getIndex());
    }

    /**
     * Adds a {@link Container} to the window using its intrinsic index, but associates it
     * with a pre-compiled JavaFX Node. Useful for performance optimizations.
     * @param container The container metadata and reference.
     * @param node The pre-compiled JavaFX Node to render.
     */
    public void addContainer(Container container, Node node) {
        addContainer(container, node, container.getIndex());
    }

    /**
     * Adds a {@link Container} to the window at a specific index. If a container already exists at the given index,
     * it recursively shifts existing containers up one index to accommodate the new one.
     * @param container The container to add.
     * @param index The desired rendering index (z-order) for the container.
     */
    public void addContainer(Container container, int index) {
        Container current = containers.get(index);
        if (current != null) {
            int i = index + 1;
            removeContainer(current);
            addContainer(current, i);
        }
        containers.put(index, container);
        container.setWindow(this); // Store window reference.

        Node assemble;
        // Check for cached node.
        if (container.getNode() != null) {
            assemble = container.getNode();
        } else {
            assemble = container.assemble();
        }

        if (index > 0) {
            if (root.getChildren().size() < index) {
                root.getChildren().addLast(assemble);
            } else {
                root.getChildren().add(index, assemble);
            }
        } else {
            root.getChildren().add(assemble);
        }
    }

    /**
     * Adds a pre-compiled {@link Container} to the window at a specific index.
     * Use {@link Container#assemble()} to build the {@link Node}. Nodes are automatically
     * assembled when the base container is drawn to the screen.
     * If a Container is large or executes a long task, it might freeze or lock the UI.
     * You can assemble the Container asynchronously to prevent UI freezing.
     *
     * <pre>
     * {@code
     * Container container = new EmptyContainer(100, 100);
     * // Add elements to the container.
     *
     * runTaskAsynchronously(() -> {
     * Node assemble = container.assemble();
     * Platform.runLater(() -> {
     * window.addContainer(container, assemble, 0);
     * });
     * });
     * // Display a loading view which can be removed in the task above.
     * }
     * </pre>
     *
     * @param container The container context to track.
     * @param node The pre-compiled node to visually add.
     * @param index The desired rendering index (z-order) for the container.
     */
    public void addContainer(Container container, Node node, int index) {
        Container current = containers.get(index);
        if (current != null) {
            int i = index + 1;
            removeContainer(current);
            addContainer(current, i);
        }
        containers.put(index, container);
        root.getChildren().add(node);
    }

    /**
     * Adds a collection of containers from the given TreeMap to this window.
     * Existing containers with matching indices will be overwritten in the internal map.
     * @param con The TreeMap of containers to add.
     */
    public void addContainers(TreeMap<Integer, Container> con) {
        this.containers.putAll(con);
    }

    /**
     * Replaces the entire set of containers in the window with a new TreeMap of containers.
     * @param containers The new TreeMap of containers to track.
     */
    public void setContainers(TreeMap<Integer, Container> containers) {
        this.containers = containers;
    }

    /**
     * Replaces an existing container instance with a new container instance, preserving its original index.
     * The old container must already exist in the window's collection for the replacement to occur.
     * @param oldContainer The container instance to be replaced.
     * @param newContainer The new container instance to take its place.
     */
    public void replaceContainer(Container oldContainer, Container newContainer) {
        if (containers.containsValue(oldContainer)) {
            containers.replace(oldContainer.getIndex(), newContainer);
        }
    }

    /**
     * Replaces the container at a specific index with a new container.
     * The window is then re-rendered to reflect this visual change.
     * @param index The index at which to replace the container.
     * @param container The new container to place at the specified index.
     */
    public void replaceContainer(int index, Container container) {
        containers.remove(index);
        containers.replace(index, container);
        render();
    }

    /**
     * Removes a specific {@link Container} instance from the window's collection.
     * Note: This removes the container from the internal map and its corresponding JavaFX
     * Node from the root's children, but a subsequent `render()` call might be needed to
     * ensure structural consistency depending on execution context.
     * @param container The container instance to remove.
     */
    public void removeContainer(Container container) {
        int toRemove = -1;
        for (Map.Entry<Integer, Container> entry : containers.entrySet()) {
            if (entry.getValue() == container) {
                toRemove = entry.getKey();
                root.getChildren().remove(container.getNode());
                break;
            }
        }
        containers.remove(toRemove);

        if (currentPopup == container) {
            currentPopup = null;
        }
    }

    /**
     * Clears all containers currently tracked by the window and removes them from the view.
     */
    public void clearContainers() {
        new LinkedList<>(containers.values()).forEach(this::removeContainer);
        containers.clear();
    }

    /**
     * Removes the container at a specific index from the window and triggers a re-render.
     * @param index The index of the container to remove.
     */
    public void clearContainer(int index) {
        containers.remove(index);
        render();
    }

    /**
     * Retrieves the TreeMap of all containers currently managed by the window, ordered by index.
     * @return A TreeMap mapping container indices to Container objects.
     */
    public TreeMap<Integer, Container> getContainers() {
        return containers;
    }

    /**
     * Clears all containers and resets the window's root pane and scene entirely.
     * The stage is not automatically shown after this operation.
     */
    public void clear() {
        clear(false);
    }

    /**
     * Clears all containers and resets the window's root pane and scene entirely.
     * Optionally shows the stage after the clearance process is complete.
     * @param render True to show the stage after clearing, false otherwise.
     */
    public void clear(boolean render) {
        clearContainers();
        this.root = new Pane();
        root.setPrefSize(width, height);
        this.scene = new Scene(root);
        this.stage.setScene(scene);
        if (render) {
            stage.show();
        }
    }

    /**
     * Closes the JavaFX Stage associated with this window.
     * @param handleEvent If false, unbinds the window's hidden and close request event listeners before closing.
     */
    public void close(boolean handleEvent) {
        if (stage != null) {
            if (!handleEvent) {
                stage.setOnHidden(null);
                stage.setOnCloseRequest(null);
            }
            stage.close();
        }
    }

    /**
     * Resets the visual state of the window by creating a new Stage with the original configuration parameters.
     */
    public void resetStage() {
        buildStage();
    }


    /**
     * Fully constructs the JavaFX Stage structure and renders all active nodes onto the screen.
     */
    public void buildAndRender() {
        buildStage();
        render();
    }

    /**
     * Builds and displays all active nodes on the screen. This function translates the engine's API
     * into JavaFX nodes and updates the stage and scene. Focus is requested if the window is configured for it.
     * <p>
     * Calling this excessively can cause visual flicker. Do not call this function after every element change. Only use when necessary to force a display update.
     * </p>
     */
    public void render() {
        build();
        if (focused) {
            stage.requestFocus();
        }
        stage.show();
    }

    /**
     * Translates the engine's container definitions into JavaFX nodes without immediately showing
     * the stage, provided there is at least one active container.
     */
    public void build() {
        if (!containers.isEmpty()) {
            build(false);
        }
    }

    /**
     * Iterates through active containers and applies them to the JavaFX root pane.
     * This method processes and prepares containers for display.
     * @param reset If true, fully reinitializes the root pane and scene graph (can cause flickering, but clears stale cache).
     * If false, simply clears existing children and stylesheets before reapplying containers.
     */
    public void build(boolean reset) {
        root.getChildren().clear();
        root.getStylesheets().clear();
        if (reset) {
            // Resets the scene. This can cause flickering but might be needed in specific capture scenarios.
            this.root = new Pane();
            root.setPrefSize(width, height);
            this.scene = new Scene(root);
            this.stage.setScene(scene);
        }

        containers.values().forEach(this::renderContainer);
    }

    /**
     * Adds a specific container on top of the current window's content stack.
     * The container is dynamically assigned an index representing the highest available layer.
     * @param container The container to assign and render.
     */
    public void render(Container container) {
        int index = containers.isEmpty() ? 1 : containers.lastKey() + 1;
        container.setIndex(index);
        containers.put(index, container);
        renderContainer(container);
    }

    /**
     * Assembles a single {@link Container} instance into a JavaFX Node and appends it to the window's root pane.
     * Removes the previous node instance from the root if it existed.
     * @param container The container to translate and render.
     */
    private void renderContainer(Container container) {
        if (container.getNode() != null) {
            root.getChildren().remove(container.getNode());
        }

        root.getChildren().add(container.assemble());
    }

    /**
     * Internal handler to render a container explicitly as a singular active popup.
     * Removes the currently tracked popup container before registering the new one.
     * @param container The container functioning as the popup layout.
     */
    private void renderPopupContainer(Container container) {
        if (currentPopup != null) {
            removeContainer(currentPopup);
        }
        currentPopup = container;

        addContainer(container);
    }

    /**
     * Computes positioning and renders a basic popup overlay dynamically onto the screen.
     * @param overlay The {@link Overlay} content to embed inside the popup.
     * @param position A predefined {@link PopupPosition} layout value.
     * @param width The targeted width of the popup frame.
     * @param height The targeted height of the popup frame.
     * @param autoClose If true, the popup automatically dismisses itself after a set duration.
     */
    public void renderPopup(Overlay overlay, PopupPosition position, double width, double height, boolean autoClose) {
        renderPopup(overlay, position, width, height, autoClose, null);
    }

    /**
     * Computes the X and Y coordinate logic for a popup based on the window's dimensions and scaling parameters,
     * then delegates the drawing instructions to absolute coordinate rendering.
     * @param overlay The {@link Overlay} logic.
     * @param position The geographical screen layout {@link PopupPosition} logic.
     * @param width Desired popup width.
     * @param height Desired popup height.
     * @param autoClose True if the popup should auto-dismiss on a timer.
     * @param label An optional {@link TextOverlay} to accompany the popup.
     */
    public void renderPopup(Overlay overlay, PopupPosition position, double width, double height, boolean autoClose, TextOverlay label) {
        // Get current window dimensions (these are in the logical, unscaled coordinate system)
        double windowWidth = this.width;
        double windowHeight = this.height;

        // Calculate x and y based on the desired position (in unscaled coordinates)
        double calculatedX;
        double calculatedY = switch (position) {
            case TOP_CENTER -> {
                calculatedX = (windowWidth - width) / 2;
                yield 0;
            }
            case BOTTOM_CENTER -> {
                calculatedX = (windowWidth - width) / 2;
                yield (windowHeight - height) - 60;
            }
            case LEFT_CENTER -> {
                calculatedX = 0;
                yield (windowHeight - height) / 2;
            }
            case RIGHT_CENTER -> {
                calculatedX = windowWidth - width;
                yield (windowHeight - height) / 2;
            }
            default -> {
                calculatedX = (windowWidth - width) / 2;
                yield (windowHeight - height) / 2;
            }
        };

        // Adjust calculatedX and calculatedY for the root Pane's current scaling
        double currentScaleX = getWidthScale();
        double currentScaleY = getHeightScale();

        // Prevent division by zero if scales are not yet initialized or are zero
        if (currentScaleX == 0) currentScaleX = 1.0;
        if (currentScaleY == 0) currentScaleY = 1.0;

        // Apply inverse scaling to the translation values
        // These are the final values that CardContainer.build() will use for its internal Pane's translation.
        calculatedX /= currentScaleX;
        calculatedY /= currentScaleY;

        renderPopup(overlay, calculatedX, calculatedY, width, height, autoClose, label);
    }

    /**
     * Encapsulates an overlay into a dynamically constructed {@link EmptyContainer} positioned at exact coordinates,
     * registering it as the active top-level popup element. Assigns lifecycle event hooks based on overlay types.
     * @param overlay The {@link Overlay} element to draw.
     * @param x The specific scaled horizontal X coordinate.
     * @param y The specific scaled vertical Y coordinate.
     * @param width The overall width boundary of the popup constraint container.
     * @param height The overall height boundary of the popup constraint container.
     * @param autoClose True to queue a removal thread via JavaFX timeline after 10,000 milliseconds.
     * @param label Optional text overlay addition.
     */
    public void renderPopup(Overlay overlay, double x, double y, double width, double height, boolean autoClose, TextOverlay label) {
        EmptyContainer container = new EmptyContainer(x, y, width, height);
        container.setPrefSize(width, height);
        container.setMaxSize(width, height);

        if (currentPopup != null) {
            removeContainer(currentPopup);
        }
        currentPopup = container;


        int index = containers.isEmpty() ? 1 : containers.lastKey() + 1;
        container.setIndex(index);

        if (overlay instanceof MessageOverlay messageOverlay) {
            messageOverlay.onClose(event -> removeContainer(container));
        } else if (overlay instanceof NotificationOverlay notificationOverlay) {
            notificationOverlay.onClose(event -> removeContainer(container));
        }

        if (autoClose) {
            long defaultCloseDelayMs = 10000;
            Timeline timeline = new Timeline(new KeyFrame(
                    Duration.millis(defaultCloseDelayMs),
                    event -> {
                        // Ensure removal happens on the JavaFX Application Thread
                        if (containers.containsValue(container)) {
                            Platform.runLater(() -> {
                                removeContainer(container);
                                render();
                            });
                        }
                    }
            ));
            timeline.play();
        }

        container.addElement(overlay);

        if (label != null) {
            container.addElement(label);
        }

        addContainer(container);
    }

    /**
     * Evaluates a pre-existing container directly into the application space as an exclusive top-level popup at specific coordinates.
     * @param container The assembled container logic to set as active.
     * @param x Translated X coordinate.
     * @param y Translated Y coordinate.
     * @param width Bounding box width.
     * @param height Bounding box height.
     */
    public void renderPopup(Container container, double x, double y, double width, double height) {
        if (currentPopup != null) {
            removeContainer(currentPopup);
        }
        currentPopup = container;
        container.setX(x);
        container.setY(y);
        container.setPrefSize(width, height);
        container.setMaxSize(width, height);

        int index = containers.isEmpty() ? 1 : containers.lastKey() + 1;
        container.setIndex(index);
        container.assemble();
        addContainer(container);
    }

    /**
     * Handles complex inverse scaling math and layout configuration to translate a requested predefined {@link PopupPosition}
     * onto exact coordinate mappings, then delegates the raw drawing instructions to the coordinate popup renderer.
     * @param container The container acting as a custom popup view.
     * @param position The conceptual mapping for the popup on screen.
     * @param width The target display width.
     * @param height The target display height.
     */
    public void renderPopup(Container container, PopupPosition position, double width, double height) {
        double windowWidth = this.width;
        double windowHeight = this.height;

        double calculatedX;
        double calculatedY = switch (position) {
            case TOP_CENTER -> {
                calculatedX = (windowWidth - width) / 2;
                yield 0;
            }
            case BOTTOM_CENTER -> {
                calculatedX = (windowWidth - width) / 2;
                yield (windowHeight - height) - 60;
            }
            case LEFT_CENTER -> {
                calculatedX = 0;
                yield (windowHeight - height) / 2;
            }
            case RIGHT_CENTER -> {
                calculatedX = windowWidth - width;
                yield (windowHeight - height) / 2;
            }
            default -> {
                calculatedX = (windowWidth - width) / 2;
                yield (windowHeight - height) / 2;
            }
        };

        double currentScaleX = getWidthScale();
        double currentScaleY = getHeightScale();

        if (currentScaleX == 0) currentScaleX = 1.0;
        if (currentScaleY == 0) currentScaleY = 1.0;

        calculatedX /= currentScaleX;
        calculatedY /= currentScaleY;

        renderPopup(container, calculatedX, calculatedY, width, height);

    }

    /**
     * Halts application workflow to trigger and display a system-level Alert dialog.
     * @param alertOverlay The overlay containing the configured {@link Alert} structure.
     */
    public void renderAlert(AlertOverlay alertOverlay) {
        Alert alert = alertOverlay.getAlert();
        alert.showAndWait();
    }

    /**
     * Retrieves the instance of the current active popup container traversing the engine window layer.
     * @return The currently displaying popup {@link Container}, or null if none exist.
     */
    public Container getCurrentPopup() {
        return currentPopup;
    }

    /**
     * Injects a custom window resize handler logic into the application window lifecycle.
     * Overrides any previously configured resize hooks.
     * @param windowResize The implementation defining custom resize handling logic.
     */
    public void onWindowResize(IWindowResize windowResize) {
        this.windowResize = windowResize;
    }

    /**
     * Retrieves the custom window resize handler currently bound to the window.
     * @return The active {@link IWindowResize} implementation, or null if unassigned.
     */
    public IWindowResize getWindowResize() {
        return windowResize;
    }

    /**
     * Listens to the core JavaFX Stage dimensions to maintain structural integrity.
     * If the scaling config is set to true, this dynamically applies affine transforms directly to the root pane
     * to stretch the application content smoothly. Otherwise, it delegates resolution changes to a custom event dispatcher.
     */
    private void handleWindowScaling() {
        scene.heightProperty().addListener((observable, oldValue, newValue) -> {
            this.height = newValue.doubleValue();

            double scaleWidth = getWidthScale();
            double scaleHeight = getHeightScale();

            if (scale) {
                // Renamed to 'scaleTransform' to prevent shadowing the 'this.scale' boolean
                Scale scaleTransform = new Scale(scaleWidth, scaleHeight, 0, 0);
                root.getTransforms().setAll(scaleTransform);
            } else {
                if (getWindowResize() != null) {
                    WindowResizeEvent event = new WindowResizeEvent(this, oldValue, newValue);
                    getWindowResize().onWindowResize(event);
                }
            }
        });

        scene.widthProperty().addListener((observable, oldValue, newValue) -> {
            this.width = newValue.doubleValue();

            double scaleWidth = getWidthScale();
            double scaleHeight = getHeightScale();

            if (scale) {
                Scale scaleTransform = new Scale(scaleWidth, scaleHeight, 0, 0);
                root.getTransforms().setAll(scaleTransform);
            } else {
                if (getWindowResize() != null) {
                    WindowResizeEvent event = new WindowResizeEvent(this, oldValue, newValue);
                    getWindowResize().onWindowResize(event);
                }
            }
        });
    }
}