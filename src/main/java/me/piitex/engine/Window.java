package me.piitex.engine;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import me.piitex.engine.hanlders.events.ScrollDownEvent;
import me.piitex.engine.hanlders.events.ScrollUpEvent;
import me.piitex.engine.layouts.Layout;
import me.piitex.engine.loaders.ImageLoader;
import me.piitex.engine.overlays.Overlay;

import java.io.File;
import java.net.MalformedURLException;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Window is the main GUI component which handle the rendering process for the engine. There are three components to windows which are {@link Container}, {@link Overlay}, {@link Layout}.
 * Window houses and manages these components.
 * <p>
 * You can create and render multiple windows at once. The title is used for the process name and label in the top left corner.
 * The stage style is used to control how the window is displayed. A decorated style will contain a "X", minimize, and maximize button.
 * An undecorated style will not contain any top bar similar to a full-screen game.
 * <pre>
 * {@code
 * Window window = new WindowBuild("My Application Window")
 *     .setStageStyle(StageStyle.UNDECORATED)
 *     .setDimensions(800, 600)
 *     .setBackgroundColor(Color.BLACK)
 *     .setCaptureInput(true)
 *     .build();
 * }
 * </pre>
 * <p>
 * To display various elements to a window you must create a container first. Once the container is created you simply have to add it the window.
 * Note, you can add and position multiple containers to a single window.
 * <pre>
 * {@code
 *  Window window = application.getWindow();
 *  Container container = new EmptyContainer(x, y, width, height);
 *  window.addContainer(container);
 * }
 * </pre>
 * <p>
 * All GUI related functions must be called in the JavaFX thread.
 * <pre>
 * {@code
 *  Tasks.runAsync(() -> {
 *      // Some code to be ran asynchronously
 *
 *      // Handle JavaFX in async
 *      Tasks.runJavaFXThread(() -> {
 *          // Gui related code
 *          window.render();
 *      })
 *  })
 * }
 * </pre>
 *
 * @see Container
 * @see Overlay
 * @see Layout
 */
public class Window {
    private final String title;
    private final ImageLoader icon;
    private final StageStyle stageStyle;
    private final int width, height;
    private boolean fullscreen = false, maximized = false;
    private Color backgroundColor = Color.BLACK;
    private Stage stage;
    private Scene scene;
    private Pane root;

    // Time tracking for thresholds
    private Instant lastRun;
    private Instant firstRun;
    private boolean captureInput = true;

    private LinkedHashMap<Integer, Container> containers = new LinkedHashMap<>();

    private boolean focused = true;

    // Used for window scaling
    private double currentHeight = 1080, currentWidth = 1920;


    /**
     * Creates a stylized window using the {@link WindowBuilder} class.
     * <p>
     * The common style is {@link StageStyle#DECORATED} which adds the basic buttons like exit, minimize, and maximize.
     * You can explore with other styles that suit your needs with the desired window.
     * </p>
     * <p>
     *     Example usage:
     *     <pre>
     *         {@code
     *           WindowBuilder builder = new WindowBuilder()
     *              .setStageStyle(StageStyle.UNDECORATED)
     *              .setDimensions(800, 600)
     *              .setBackgroundColor(Color.BLACK)
     *              .setCaptureInput(true);
     *           Window window = new Window(builder);
     *
     *           // Add containers
     *           window.addContainer(Container container);
     *           window.render();
     *         }
     *     </pre>
     * </p>
     */
    public Window(WindowBuilder builder) { //
        this.title = builder.getTitle(); //
        this.stageStyle = builder.getStageStyle(); //
        this.icon = builder.getIcon(); //
        this.width = builder.getWidth(); //
        this.height = builder.getHeight(); //
        this.backgroundColor = builder.getBackgroundColor(); //
        this.captureInput = builder.isCaptureInput(); //
        this.fullscreen = builder.isFullscreen(); //
        this.maximized = builder.isMaximized(); //
        this.focused = builder.isFocused(); //
        buildStage(); //
    }

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

        root = new BorderPane();
        scene = new Scene(root);

        stage.setScene(scene);
        if (captureInput) {
            handleStageInput(stage);
        }
    }

    public void updateBackground(Color color) {
        this.backgroundColor = color;
        root.setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
        stage.getScene().setFill(color);
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public Stage getStage() {
        return stage;
    }

    public Scene getScene() {
        return scene;
    }
    public Pane getRoot() {
        return root;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean hasCaptureInput() {
        return captureInput;
    }

    /**
     * Toggles the stage to full-screen or windowed.
     * @param fullscreen Pass true for fullscreen, false for windowed.
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

    public void addContainer(Container container, int index) {
        Container current = containers.get(index);
        if (current != null) {
            int i = index + 1;
            addContainer(getContainers().get(index), i);
        }
        containers.put(index, container);
    }

    public void addContainer(Container container) {
        addContainer(container, container.getIndex());
    }

    public void addContainers(LinkedHashMap<Integer, Container> con) {
        this.containers.putAll(con);
    }

    public void setContainers(LinkedHashMap<Integer, Container> containers) {
        this.containers = containers;
    }

    public void replaceContainer(Container oldContainer, Container newContainer) {
        if (containers.containsValue(oldContainer)) {
            containers.replace(oldContainer.getIndex(), newContainer);
        }
    }

    public void replaceContainer(int index, Container container) {
        // Remove current index
        containers.remove(index);
        containers.replace(index, container);
        render();
    }

    public void removeContainer(Container container) {
        this.containers.remove(container.getIndex());
    }

    public void clearContainers() {
        containers.clear();
        System.gc();
    }

    public void clearContainer(int index) {
        containers.remove(index);

        // Re-render
        render();
    }

    public LinkedHashMap<Integer, Container> getContainers() {
        return containers;
    }

    public void clean() {
        root.getChildren().clear();
        scene.setRoot(root);
        stage.show();
    }

    // Clears and resets current window.
    public void clear() {
        clear(false);
    }

    public void clear(boolean render) {
        clearContainers();
        this.root = new Pane();
        this.scene = new Scene(root);
        this.stage.setScene(scene);
        if (render) {
            stage.show();
        }
    }

    public void close() {
        if (stage != null) {
            stage.close();
            System.gc(); // Force garbage collection once the window is closed.
        }
    }

    public void resetStage() {
        root.getChildren().clear();
        stage = new Stage();
        stage.hide();
    }

    public void hide() {
        stage.show();
    }

    public void buildAndRender() {
        buildStage();
        render();
    }

    /**
     * Builds and displays all active nodes on the screen. Can cause flicker if called excessively. If you changed by adding, modifying, or removing {@link Overlay} or {@link Container} you must call this function.
     * This function translates RenJava API into JavaFX and updates the stage and scene.
     */
    public void render() {
        build();
        if (focused) {
            stage.requestFocus();
        }
        stage.show();
        // Force clear resources that are unused.
        // To those who feel like GC is bad practice or indicates broken code allow me to explain.
        // Garbage is automatically collected and deleted by the JVM which is good enough for most cases.
        // HOWEVER, when you are rendering and loading multiple 10mb+ images within a 5 minute time period auto GC is far too slow.
        // This call may not do anything at all at times. It tells the JVM that I want to clear any unused references pronto not when it wants to.
        // There are multiple gc calls within the framework and when testing on my own machine they dramatically decrease resource usage by 300mb+
        // I will admit that there may be in a memory leak somewhere in the framework, but this is not the solution to that.
        //
        // TL;DR I ain't waiting for your slow ass jvm to clear resources.
        System.gc();

    }

    /**
     * This function is used to build the RenJava API onto the JavaFX framework. This will not render the built nodes onto the screen. Recommended to use {@link #render()} for most use cases.
     */
    public void build() {
        build(false);
    }

    public void build(boolean reset) {
        if (containers.isEmpty()) {
            System.err.println("You must add containers to the window before every render call.");
        }

        root.getChildren().clear();
        root.getStylesheets().clear();
        if (reset) {
            // Causes flickering but needed when capturing scene.
            // Resets the scene.
            this.root = new Pane();
            this.scene = new Scene(root);
            this.stage.setScene(scene);
        }

        containers.values().forEach(this::renderContainer);
    }

    // Renders container on top of current window
    public void render(Container container) {
        int index = containers.size();
        container.setIndex(index);
        containers.put(index, container);
        renderContainer(container);
    }

    private void renderContainer(Container container) {
        Map.Entry<Node, LinkedList<Node>> entry = container.build();
        Node node = entry.getKey();

        node.prefHeight(container.getHeight());
        node.prefWidth(container.getWidth());
        node.setTranslateX(container.getX());
        node.setTranslateY(container.getY());

        for (Node n : entry.getValue()) {
            if (node instanceof Pane pane) {
                pane.getChildren().add(n);
            }
            // Different pane types
        }

        for (File file : container.getStylesheets()) {
            try {
                root.getStylesheets().add(file.toURI().toURL().toExternalForm());
            } catch (MalformedURLException e) {
                System.err.println(e.getMessage());
            }
        }

        getRoot().getChildren().add(node);

        //ContainerRenderEvent renderEvent = new ContainerRenderEvent(container, node);
        //RenJava.getEventHandler().callEvent(renderEvent);
    }

    private void handleStageInput(Stage stage) {
        stage.addEventFilter(ScrollEvent.SCROLL, event -> {
            double y = event.getDeltaY();
            if (y > 0) {
                // Scroll up
                ScrollUpEvent scrollUpEvent = new ScrollUpEvent();
                //RenJava.getEventHandler().callEvent(scrollUpEvent);
            } else {
                ScrollDownEvent downEvent = new ScrollDownEvent();
                //RenJava.getEventHandler().callEvent(downEvent);
            }
        });

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