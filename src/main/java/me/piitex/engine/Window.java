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
import me.piitex.engine.containers.EmptyContainer;
import me.piitex.engine.hanlders.events.ContainerRenderEvent;
import me.piitex.engine.layouts.Layout;
import me.piitex.engine.loaders.ImageLoader;
import me.piitex.engine.overlays.*;

import java.time.Instant;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

/**
 * The `Window` class serves as the primary GUI component, managing the rendering process for the engine.
 * It houses and manages three core components: {@link Container}, {@link Overlay}, and {@link Layout}.
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
 * .setCaptureInput(true)
 * .build();
 * }</pre>
 *
 * <p>To display elements within a window, a {@link Container} must first be created and added to the window.
 * Multiple containers can be added and positioned within a single window.</p>
 * <pre>{@code
 * Window window = application.getWindow();
 * Container container = new EmptyContainer(x, y, width, height);
 * window.addContainer(container);
 * }</pre>
 *
 * <p>All GUI-related functions, especially those involving scene graph modifications,
 * must be executed on the JavaFX Application Thread.</p>
 * <pre>{@code
 * Tasks.runAsync(() -> {
 * // Some code to be ran asynchronously
 *
 * // Handle JavaFX updates on the JavaFX thread
 * Tasks.runJavaFXThread(() -> {
 * // GUI-related code
 * window.render();
 * })
 * })
 * }</pre>
 *
 * @see Container
 * @see Overlay
 * @see Layout
 */
public class Window {
    private final String title;
    private final ImageLoader icon;
    private final StageStyle stageStyle;
    private int width, height;
    private boolean fullscreen = false, maximized = false;
    private Color backgroundColor = Color.BLACK;
    private Stage stage;
    private Scene scene;
    private Pane root;

    private Instant lastRun;
    private Instant firstRun;
    private boolean captureInput = true;
    private boolean scale;

    private TreeMap<Integer, Container> containers = new TreeMap<>();
    private Container currentPopup = null;

    private boolean focused = true;

    /**
     * Constructs a Window instance using properties defined in a {@link WindowBuilder}.
     * This allows for a flexible and readable way to configure window properties.
     *
     * <p>Common styles include {@link StageStyle#DECORATED}, which provides standard
     * window controls, and {@link StageStyle#UNDECORATED} for a borderless window.</p>
     *
     * <p>Example usage:</p>
     * <pre>{@code
     * WindowBuilder builder = new WindowBuilder()
     * .setStageStyle(StageStyle.UNDECORATED)
     * .setDimensions(800, 600)
     * .setBackgroundColor(Color.BLACK)
     * .setCaptureInput(true);
     * Window window = new Window(builder);
     *
     * // Add containers
     * window.addContainer(someContainer);
     * window.render();
     * }</pre>
     * @param builder The {@link WindowBuilder} instance containing window configuration.
     */
    public Window(WindowBuilder builder) {
        this.title = builder.getTitle();
        this.stageStyle = builder.getStageStyle();
        this.icon = builder.getIcon();
        this.width = builder.getWidth();
        this.height = builder.getHeight();
        this.backgroundColor = builder.getBackgroundColor();
        this.captureInput = builder.isCaptureInput();
        this.fullscreen = builder.isFullscreen();
        this.maximized = builder.isMaximized();
        this.focused = builder.isFocused();
        this.scale = builder.isScale();
        buildStage();
    }

    /**
     * Initializes the JavaFX Stage with the configured properties from the `WindowBuilder`.
     * This method sets up the title, style, dimensions, icon, and initial scene.
     */
    protected void buildStage() {
        stage = new Stage();

        if (icon != null) {
            Image windowIcon = icon.buildRaw();
            if (windowIcon != null) {
                stage.getIcons().add(windowIcon);
            }
        }
        stage.setTitle(title);
        stage.initStyle(stageStyle);
        stage.setWidth(width);
        stage.setHeight(height);
        stage.setMaximized(maximized);
        stage.setFullScreen(fullscreen);

        root = new Pane();
        root.setPrefSize(width, height);

        if (scale) {
            double scaleWidth = (double) width / RenConfiguration.getWidth();
            double scaleHeight = (double) height / RenConfiguration.getHeight();
            Scale scale = new Scale(scaleWidth, scaleHeight, 0, 0);
            root.getTransforms().setAll(scale);

            handleWindowScaling(stage);
        }

        scene = new Scene(root);

        stage.setScene(scene);
    }

    /**
     * Updates the background color of the window's root pane and scene.
     * @param color The new background color.
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
     * Retrieves the configured width of the window.
     * @return The window width.
     */
    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
        stage.setWidth(width);
    }

    public void setHeight(int height) {
        this.height = height;
        stage.setHeight(height);
    }

    /**
     * Retrieves the configured height of the window.
     * @return The window height.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Checks if the window is configured to capture input events.
     * @return true if input capture is enabled, false otherwise.
     */
    public boolean hasCaptureInput() {
        return captureInput;
    }

    /**
     * Toggles the window between full-screen and windowed modes.
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
     * Adds a {@link Container} to the window at a specific index. If a container already exists at the given index,
     * it attempts to shift existing containers to accommodate the new one.
     * @param container The container to add.
     * @param index The desired rendering index for the container.
     */
    public void addContainer(Container container, int index) {
        Container current = containers.get(index);
        if (current != null) {
            int i = index + 1;
            addContainer(getContainers().get(index), i);
        }
        containers.put(index, container);
    }

    /**
     * Adds a {@link Container} to the window using its intrinsic index.
     * @param container The container to add.
     */
    public void addContainer(Container container) {
        addContainer(container, container.getIndex());
    }

    /**
     * Adds all containers from the given TreeMap to this window's container collection.
     * Existing containers with matching indices will be overwritten.
     * @param con The TreeMap of containers to add.
     */
    public void addContainers(TreeMap<Integer, Container> con) {
        this.containers.putAll(con);
    }

    /**
     * Replaces the entire set of containers in the window with a new TreeMap of containers.
     * @param containers The new TreeMap of containers.
     */
    public void setContainers(TreeMap<Integer, Container> containers) {
        this.containers = containers;
    }

    /**
     * Replaces an old container instance with a new container instance, preserving its original index.
     * The old container must already exist in the window's collection.
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
     * The window is then re-rendered to reflect this change.
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
     * Note: This only removes the container from the internal map,
     * it does not automatically remove its corresponding JavaFX Node from the scene graph.
     * A subsequent `render()` call would be needed to update the display.
     * @param container The container instance to remove.
     */
    public void removeContainer(Container container) {
        int toRemove = -1;
        for (Map.Entry<Integer, Container> entry : containers.entrySet()) {
            if (entry.getValue() == container) {
                toRemove = entry.getKey();
                root.getChildren().remove(container.getView());
                break;
            }
        }
        containers.remove(toRemove);


        // Assuming it's a popup
        currentPopup = null;
    }

    /**
     * Clears all containers from the window.
     * A garbage collection hint is provided to the JVM.
     */
    public void clearContainers() {
        containers.clear();
        System.gc();
    }

    /**
     * Removes the container at a specific index from the window and re-renders the display.
     * @param index The index of the container to remove.
     */
    public void clearContainer(int index) {
        containers.remove(index);
        render();
    }

    /**
     * Retrieves the TreeMap of all containers currently managed by the window.
     * @return A TreeMap mapping container indices to Container objects.
     */
    public TreeMap<Integer, Container> getContainers() {
        return containers;
    }

    /**
     * Clears all child nodes from the root pane and re-sets the scene's root.
     * The stage is then shown.
     */
    public void clean() {
        root.getChildren().clear();
        scene.setRoot(root);
        stage.show();
    }

    /**
     * Clears all containers and resets the window's root pane and scene.
     * The stage is not automatically shown after this operation.
     */
    public void clear() {
        clear(false);
    }

    /**
     * Clears all containers and resets the window's root pane and scene.
     * Optionally shows the stage after clearing.
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
     * A garbage collection hint is provided to the JVM.
     */
    public void close() {
        if (stage != null) {
            stage.close();
            System.gc();
        }
    }

    /**
     * Clears the root pane's children, creates a new Stage, and hides it.
     * This method essentially resets the visual state of the window without closing it.
     */
    public void resetStage() {
        buildStage();
    }

    /**
     * Shows the window's stage.
     * Note: This method is named "hide" but performs "show". This might be a naming inconsistency.
     */
    public void hide() {
        stage.show();
    }

    /**
     * Builds the JavaFX Stage and then renders all active nodes on the screen.
     */
    public void buildAndRender() {
        buildStage();
        render();
    }

    /**
     * Builds and displays all active nodes on the screen. This function translates the engine's API
     * into JavaFX and updates the stage and scene.
     * <p>
     * Calling this excessively can cause visual flicker. It must be called after adding,
     * modifying, or removing {@link Overlay} or {@link Container} to update the display.
     * </p>
     */
    public void render() {
        build();
        if (focused) {
            stage.requestFocus();
        }
        stage.show();
        // The System.gc() call is commented out but left for reference.
        // It provides a hint to the JVM to run the garbage collector.
        // While not generally recommended for performance-critical applications due to JVM's
        // optimized automatic garbage collection, it was noted in original comments to
        // potentially help with resource usage in specific scenarios involving large assets.
        // It's important to understand that explicit GC calls are not a solution for memory leaks.
        System.gc();
    }

    /**
     * Builds the engine's API onto the JavaFX framework without displaying the built nodes on the screen.
     * For most use cases, {@link #render()} is recommended as it also shows the updated display.
     */
    public void build() {
        build(false);
    }

    /**
     * Builds the engine's API onto the JavaFX framework, optionally resetting the scene.
     * This method processes and prepares containers for display but does not automatically show them.
     * @param reset True to reset the scene (clears and reinitializes the root pane and scene),
     * false to only clear children.
     */
    public void build(boolean reset) {
        if (containers.isEmpty()) {
            System.err.println("You must add containers to the window before every render call.");
        }

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
     * Renders a specific container by adding it on top of the current window's content.
     * The container is automatically assigned an index that places it at the highest layer.
     * @param container The container to render.
     */
    public void render(Container container) {
        int index = containers.isEmpty() ? 1 : containers.lastKey() + 1;
        container.setIndex(index);
        containers.put(index, container);
        renderContainer(container);
    }

    /**
     * Renders a single {@link Container} instance by building its corresponding JavaFX Node
     * and adding it to the window's root pane.
     * @param container The container to render.
     */
    private void renderContainer(Container container) {
        Map.Entry<Node, LinkedList<Node>> entry = container.build();
        Node node = entry.getKey();
        node.getStyleClass().addAll(container.getStyles());
        container.setView(node);

        node.prefHeight(container.getHeight());
        node.prefWidth(container.getWidth());
        node.setTranslateX(container.getX());
        node.setTranslateY(container.getY());

        if (node instanceof Pane pane) {
            pane.setPrefWidth(container.getWidth());
            pane.setPrefHeight(container.getHeight());

            if (container.getBackgroundColor() != null) {
                pane.setBackground(new Background(new BackgroundFill(container.getBackgroundColor(), CornerRadii.EMPTY, Insets.EMPTY)));
            }
            pane.getChildren().addAll(entry.getValue());
        }

        root.getStyleClass().addAll(container.getStyles());

        root.getStylesheets().addAll(container.getStylesheets());

        getRoot().getChildren().add(node);

        if (container.getOnRender() != null) {
            container.getOnRender().onContainerRender(new ContainerRenderEvent(container, entry.getKey()));
        }
    }

    /**
     * Renders a popup container, ensuring that only one popup is active at a time.
     * If a previous popup exists, its Node is removed from the scene graph before the new one is added.
     * This method does not apply translation (X, Y) from the container's properties directly to the node,
     * assuming these are handled by the calling `renderPopup` method.
     * @param container The container to render as a popup.
     */
    private void renderPopupContainer(Container container) {
        if (currentPopup != null) {
            removeContainer(currentPopup);
        }
        Map.Entry<Node, LinkedList<Node>> entry = container.build();
        Node node = entry.getKey();
        container.setView(node);
        currentPopup = container;

        if (node instanceof Pane pane) {
            pane.setPrefWidth(container.getWidth());
            pane.setPrefHeight(container.getHeight());

            if (container.getBackgroundColor() != null) {
                pane.setBackground(new Background(new BackgroundFill(container.getBackgroundColor(), CornerRadii.EMPTY, Insets.EMPTY)));
            }
            for (Node n : entry.getValue()) {
                pane.getChildren().add(n);
            }
        }
        root.getStylesheets().addAll(container.getStylesheets());

        getRoot().getChildren().add(node);
    }

    public void renderPopup(Overlay overlay, PopupPosition position, double width, double height, boolean autoClose) {
        renderPopup(overlay, position, width, height, autoClose, null);
    }

    /**
     * Renders a popup with an overlay content, and desired position.
     * This method calculates the popup's position based on window dimensions and scaling,
     * then creates and renders a {@link Container} to house the overlay.
     * @param overlay The {@link Overlay} content to display within the popup.
     * @param position The desired position of the popup on the screen.
     */
    public void renderPopup(Overlay overlay, PopupPosition position, double width, double height, boolean autoClose, TextOverlay label) {
        // Get current window dimensions (these are in the logical, unscaled coordinate system)
        double windowWidth = this.width;
        double windowHeight = this.height;

        // Calculate x and y based on the desired position (in unscaled coordinates)
        double calculatedX = 0;
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
        double currentScaleX = RenConfiguration.getWidthScale();
        double currentScaleY = RenConfiguration.getHeightScale();

        // Prevent division by zero if scales are not yet initialized or are zero
        if (currentScaleX == 0) currentScaleX = 1.0;
        if (currentScaleY == 0) currentScaleY = 1.0;

        // Apply inverse scaling to the translation values
        // These are the final values that CardContainer.build() will use for its internal Pane's translation.
        calculatedX /= currentScaleX;
        calculatedY /= currentScaleY;

        renderPopup(overlay, calculatedX, calculatedY, width, height, autoClose, label);
    }

    public void renderPopup(Overlay overlay, double x, double y, double width, double height, boolean autoClose, TextOverlay label) {
        EmptyContainer container = new EmptyContainer(x, y, width, height);
        container.setPrefSize(width, height);
        int index = containers.isEmpty() ? 1 : containers.lastKey() + 1;
        container.setIndex(index);

//        if (overlay instanceof Region region) {
//            region.setMaxWidth(width);
//            region.setMaxHeight(height);
//        }

        if (overlay instanceof MessageOverlay messageOverlay) {
            messageOverlay.onClose(event -> {
                removeContainer(container);
            });
        } else if (overlay instanceof NotificationOverlay notificationOverlay) {
            notificationOverlay.onClose(event -> {
                removeContainer(container);
            });
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

        containers.put(index, container);
        renderPopupContainer(container);
    }

    public void renderPopup(Container container, PopupPosition position, double width, double height) {
        double windowWidth = this.width;
        double windowHeight = this.height;

        // Calculate x and y based on the desired position (in unscaled coordinates)
        double calculatedX = 0;
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

        double currentScaleX = RenConfiguration.getWidthScale();
        double currentScaleY = RenConfiguration.getHeightScale();

        if (currentScaleX == 0) currentScaleX = 1.0;
        if (currentScaleY == 0) currentScaleY = 1.0;

        calculatedX /= currentScaleX;
        calculatedY /= currentScaleY;

        container.setX(calculatedX);
        container.setY(calculatedY);

        int index = containers.isEmpty() ? 1 : containers.lastKey() + 1;
        container.setIndex(index);

        containers.put(index, container);
        renderPopupContainer(container);
    }

    public void renderAlert(AlertOverlay alertOverlay) {
        Alert alert = alertOverlay.build();
        alert.showAndWait();
    }

    public Container getCurrentPopup() {
        return currentPopup;
    }

    /**
     * Handles various input events for the stage, including scroll events and dimension changes.
     * This method applies scaling transformations to the root pane based on window resizing.
     * @param stage The JavaFX Stage to handle input for.
     */
    private void handleWindowScaling(Stage stage) {
        // This scales the application to the desired width and height that it is running at.
        stage.heightProperty().addListener((observable, oldValue, newValue) -> {
            RenConfiguration.setCurrentWindowHeight(newValue.doubleValue());

            double scaleWidth = RenConfiguration.getWidthScale();
            double scaleHeight = newValue.doubleValue() / RenConfiguration.getHeight();

            Scale scale = new Scale(scaleWidth, scaleHeight, 0, 0);
            root.getTransforms().setAll(scale);
        });
        stage.widthProperty().addListener((observable, oldValue, newValue) -> {
            RenConfiguration.setCurrentWindowWidth(newValue.doubleValue());

            double scaleWidth = newValue.doubleValue() / RenConfiguration.getWidth();
            double scaleHeight = RenConfiguration.getHeightScale();

            Scale scale = new Scale(scaleWidth, scaleHeight, 0, 0);
            root.getTransforms().setAll(scale);
        });
    }
}