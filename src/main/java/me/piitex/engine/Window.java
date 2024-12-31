package me.piitex.engine;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import me.piitex.engine.hanlders.IWindowKey;
import me.piitex.engine.hanlders.events.WindowKeyPressEvent;
import me.piitex.engine.hanlders.events.WindowKeyReleaseEvent;
import me.piitex.engine.loaders.ImageLoader;
import me.piitex.engine.overlays.Overlay;

import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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

    // Time tracking for thresholds
    private Instant lastRun;
    private Instant firstRun;
    private boolean captureInput = true;

    private LinkedList<Container> containers = new LinkedList<>();

    private boolean focused = true;

    // Integrated handlers
    private IWindowKey windowKeyPress;
    private IWindowKey windowKeyRelease;

    public Window(String title, StageStyle stageStyle, ImageLoader icon, int width, int height) {
        this.title = title;
        this.stageStyle = stageStyle;
        this.icon = icon;
        this.width = width;
        this.height = height;
        buildStage();
    }

    public Window(String title, StageStyle stageStyle, ImageLoader icon, int width, int height, boolean captureInput) {
        this.title = title;
        this.stageStyle = stageStyle;
        this.icon = icon;
        this.width = width;
        this.height = height;
        this.captureInput = captureInput;
        buildStage();
    }

    public Window(String title, Color backgroundColor, StageStyle stageStyle, ImageLoader icon, int width, int height) {
        this.title = title;
        this.backgroundColor = backgroundColor;
        this.stageStyle = stageStyle;
        this.icon = icon;
        this.width = width;
        this.height = height;
        buildStage();
    }

    public Window(String title, Color backgroundColor, StageStyle stageStyle, ImageLoader icon, int width, int height, boolean captureInput) {
        this.title = title;
        this.backgroundColor = backgroundColor;
        this.stageStyle = stageStyle;
        this.icon = icon;
        this.width = width;
        this.height = height;
        this.captureInput = captureInput;
        buildStage();
    }

    public Window(String title, Color backgroundColor, StageStyle stageStyle, ImageLoader icon, int width, int height, boolean captureInput, boolean focused) {
        this.title = title;
        this.backgroundColor = backgroundColor;
        this.stageStyle = stageStyle;
        this.icon = icon;
        this.width = width;
        this.height = height;
        this.captureInput = captureInput;
        this.focused = focused;
        buildStage();
    }

    public Window(String title, StageStyle stageStyle, ImageLoader icon, int width, int height, boolean captureInput, boolean focused) {
        this.title = title;
        this.stageStyle = stageStyle;
        this.icon = icon;
        this.width = width;
        this.height = height;
        this.captureInput = captureInput;
        this.focused = focused;
        buildStage();
    }

    public Window onWindowKeyPress(IWindowKey event) {
        this.windowKeyPress = event;
        return this;
    }

    public Window onWindowKeyRelease(IWindowKey event) {
        this.windowKeyRelease = event;
        return this;
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

        root.setBackground(new Background(new BackgroundFill(backgroundColor, CornerRadii.EMPTY, Insets.EMPTY)));

        scene = new Scene(root);

        scene.setFill(Color.BLACK);

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

    public void addContainer(Container container) {
        this.containers.add(container);
    }

    public void addContainers(Container... containers) {
        this.containers.addAll(List.of(containers));
    }

    public void addContainers(LinkedList<Container> cont) {
        this.containers.addAll(cont);
    }

    public void setContainers(LinkedList<Container> containers) {
        this.containers = containers;
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

    public LinkedList<Container> getContainers() {
        return containers;
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
        root.getChildren().clear();
        if (reset) {
            // Causes flickering but needed when capturing scene.
            // Resets the scene.
            this.root = new Pane();
            this.scene = new Scene(root);
            this.stage.setScene(scene);
        }

        // Gather orders
        LinkedList<Container> lowOrder = new LinkedList<>();
        LinkedList<Container> normalOrder = new LinkedList<>();
        LinkedList<Container> highOrder = new LinkedList<>();

        for (Container container : containers) {
            if (container.getOrder() == DisplayOrder.LOW) {
                lowOrder.add(container);
            } else if (container.getOrder() == DisplayOrder.NORMAL) {
                normalOrder.add(container);
            } else if (container.getOrder() == DisplayOrder.HIGH) {
                highOrder.add(container);
            }
        }


        lowOrder.forEach(this::renderContainer);
        normalOrder.forEach(this::renderContainer);
        highOrder.forEach(this::renderContainer);

        // Not sure if this will cause issues but to reduce resource usage the mappings need to be cleared
        lowOrder.clear();
        normalOrder.clear();
        highOrder.clear();
    }

    // Renders container on top of current window
    public void render(Container container) {
        containers.add(container);
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

        getRoot().getChildren().add(node);
    }

    private void handleStageInput(Stage stage) {
        stage.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (windowKeyPress != null) {
                windowKeyPress.onKeyPress(new WindowKeyPressEvent(this, event));
            }
        });
        stage.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
            if (windowKeyRelease != null) {
                windowKeyRelease.onKeyRelease(new WindowKeyReleaseEvent(this, event));
            }
        });
    }

}