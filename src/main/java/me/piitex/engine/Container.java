package me.piitex.engine;

import javafx.scene.Node;
import me.piitex.engine.containers.EmptyContainer;
import me.piitex.engine.containers.handlers.IContainerClick;
import me.piitex.engine.containers.handlers.IContainerRender;
import me.piitex.engine.layouts.Layout;
import me.piitex.engine.overlays.Overlay;

import java.io.File;
import java.net.MalformedURLException;
import java.util.*;

/**
 * The container houses all the elements that render onto the {@link Window}. The class can be extended to support different containers that can handle rendering differently.
 * The default base container is the {@link EmptyContainer}. It does not have any special rendering properties.
 * <pre>
 * {@code
 *      EmptyContainer container = new EmptyContainer(double width, double height);
 *      // Add elements to the container.
 * }
 * </pre>
 * <p>
 *     The {@link Window} will have to render and handle the container. The two components work in unison.
 * <p>
 * <pre>
 * {@code
 *      Container container = new EmptyContainer(1920, 1080);
 *
 *      window.addContainer(container);
 *
 *      window.render();
 * }
 * </pre>
 */
public abstract class Container extends Renderer {
    private double x, y;

    // Set container to a debug state. This will output the rendering process.
    public boolean debug = false;

    private final List<String> stylesheets = new ArrayList<>();

    private IContainerClick click;
    private List<IContainerRender> renderEvents = new LinkedList<>();

    public Container(Node view, double x, double y, double width, double height) {
        setView(view);
        this.x = x;
        this.y = y;
        setWidth(width);
        setHeight(height);
    }

    public Container(Node view, double x, double y, double width, double height, int index) {
        setView(view);
        this.x = x;
        this.y = y;
        setWidth(width);
        setHeight(height);
        setIndex(index);
    }


    /**
     * @return The x position of the container in correlation to the window.
     */
    public double getX() {
        return x;
    }

    /**
     * Set the x position of the container.
     * @param x The x position.
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * @return The y position of the container in correlation to the window.
     */
    public double getY() {
        return y;
    }

    /**
     * Set the y position of the container.
     * @param y The y position.
     */
    public void setY(double y) {
        this.y = y;
    }

    public IContainerClick getOnClick() {
        return click;
    }

    public void onClick(IContainerClick click) {
        this.click = click;
    }

    public void addRenderEvent(IContainerRender renderEvent) {
        if (renderEvent != null) {
            this.renderEvents.add(renderEvent);
        }
    }

    public void removeRenderEvent(IContainerRender renderEvent) {
        if (renderEvent != null) {
            this.renderEvents.remove(renderEvent);
        }
    }

    public List<IContainerRender> getRenderEvents() {
        return renderEvents;
    }

    /**
     * Gets all {@link Overlay}s added to the container.
     * @return The current linked list of {@link Overlay}s.
     */
    public LinkedList<Overlay> getOverlays() {
        LinkedList<Overlay> toReturn = new LinkedList<>();
        getElements().values().stream().filter(element -> element instanceof Overlay).forEach(element -> {
            Overlay overlay = (Overlay) element;
            toReturn.add(overlay);
        });
        return toReturn;
    }

    /**
     * Gets all sub-containers for the container.
     * @return The current linked list of sub-containers.
     */
    public LinkedList<Container> getContainers() {
        LinkedList<Container> toReturn = new LinkedList<>();
        getElements().values().stream().filter(element -> element instanceof Container).forEach(element -> {
            Container container = (Container) element;
            toReturn.add(container);
        });
        return toReturn;
    }

    /**
     * Gets all {@link Layout}s for the container.
     * @return The current linked list of {@link Layout}s
     */
    public LinkedList<Layout> getLayouts() {
        LinkedList<Layout> toReturn = new LinkedList<>();
        getElements().values().stream().filter(element -> element instanceof Layout).forEach(element -> {
            Layout layout = (Layout) element;
            toReturn.add(layout);
        });
        return toReturn;
    }

    public void addStyleSheet(File file) {
        try {
            stylesheets.add(file.toURI().toURL().toExternalForm());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getStylesheets() {
        return stylesheets;
    }

    /**
     * Builds and assembles the container. Converts RenJava API into JavaFX.
     * @return An entry set where the key is the pane as a node. The value is the collection of nodes which the pane contains.
     */
    public abstract Map.Entry<Node, LinkedList<Node>> build();
}