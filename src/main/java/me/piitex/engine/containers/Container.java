package me.piitex.engine.containers;

import javafx.scene.Node;
import me.piitex.engine.Renderer;
import me.piitex.engine.Window;
import me.piitex.engine.containers.handlers.IContainerRender;
import me.piitex.engine.layouts.Layout;
import me.piitex.engine.overlays.Overlay;

import java.io.File;
import java.net.MalformedURLException;
import java.util.*;

/**
 * The container houses all the generic {@link me.piitex.engine.Element} items (like Overlays and Layouts)
 * that are meant to render onto the parent {@link Window}. The class can be extended
 * to support different containers that handle rendering in unique structural ways.
 * The default base container is the {@link EmptyContainer}. It does not have any special rendering properties.
 * <pre>
 * {@code
 * EmptyContainer container = new EmptyContainer(double width, double height);
 * // Add elements to the container.
 * }
 * </pre>
 * <p>
 * The {@link Window} will render and handle the container itself. The window specifically tracks Containers,
 * which in turn track Elements. The two components work in unison to display the scene graph.
 * </p>
 * <pre>
 * {@code
 * Container container = new EmptyContainer(1920, 1080);
 * window.addContainer(container); // Automaticallys draws to screen
 *
 * // Add elements directly to the container
 * TextOverlay text = new TextOverlay("Overlay");
 * container.addElement(text);
 * }
 * </pre>
 */
public abstract class Container extends Renderer {
    private double x, y;
    private final List<String> stylesheets = new ArrayList<>();
    private final List<IContainerRender> renderEvents = new LinkedList<>();

    /**
     * Constructs a new Container with specific positioning and dimensions.
     * Inherited properties such as width and height are managed by the parent {@link Renderer}.
     *
     * @param view   The base JavaFX Node that represents this container visually.
     * @param x      The initial horizontal (X) position relative to the parent window or root pane.
     * @param y      The initial vertical (Y) position relative to the parent window or root pane.
     * @param width  The explicitly requested width of the container.
     * @param height The explicitly requested height of the container.
     */
    public Container(Node view, double x, double y, double width, double height) {
        setNode(view);
        this.x = x;
        this.y = y;
        setX(x);
        setY(y);
        setWidth(width);
        setHeight(height);
    }

    /**
     * Constructs a new Container with specific positioning, dimensions, and a distinct rendering index (z-order).
     * Inherited properties such as width and height are managed by the parent {@link Renderer}.
     *
     * @param view   The base JavaFX Node that represents this container visually.
     * @param x      The initial horizontal (X) position relative to the parent window or root pane.
     * @param y      The initial vertical (Y) position relative to the parent window or root pane.
     * @param width  The explicitly requested width of the container.
     * @param height The explicitly requested height of the container.
     * @param index  The rendering index (z-order layer) of the container. Higher indices are rendered on top.
     */
    public Container(Node view, double x, double y, double width, double height, int index) {
        setNode(view);
        this.x = x;
        this.y = y;
        setX(x);
        setY(y);
        setWidth(width);
        setHeight(height);
        setIndex(index);
    }

    /**
     * Retrieves the explicitly assigned horizontal position of the container.
     * @return The x position of the container in correlation to the window.
     */
    public double getX() {
        return x;
    }

    /**
     * Sets the horizontal position of the container.
     * This automatically updates the translation mapping of the underlying JavaFX Node.
     *
     * @param x The new x position coordinate.
     */
    public void setX(double x) {
        this.x = x;
        getNode().setTranslateX(x);
    }

    /**
     * Retrieves the explicitly assigned vertical position of the container.
     * @return The y position of the container in correlation to the window.
     */
    public double getY() {
        return y;
    }

    /**
     * Sets the vertical position of the container.
     * This automatically updates the translation mapping of the underlying JavaFX Node.
     *
     * @param y The new y position coordinate.
     */
    public void setY(double y) {
        this.y = y;
        getNode().setTranslateY(y);
    }

    /**
     * Registers a custom render event listener to this container.
     * These events hook into the container's lifecycle to execute logic during rendering phases.
     *
     * @param renderEvent The {@link IContainerRender} implementation to attach. Ignores null values.
     */
    public void addRenderEvent(IContainerRender renderEvent) {
        if (renderEvent != null) {
            this.renderEvents.add(renderEvent);
        }
    }

    /**
     * Unregisters a specific render event listener from this container.
     *
     * @param renderEvent The {@link IContainerRender} implementation to detach. Ignores null values.
     */
    public void removeRenderEvent(IContainerRender renderEvent) {
        if (renderEvent != null) {
            this.renderEvents.remove(renderEvent);
        }
    }

    /**
     * Retrieves the collection of all render event listeners currently attached to this container.
     *
     * @return A list of configured {@link IContainerRender} handlers.
     */
    public List<IContainerRender> getRenderEvents() {
        return renderEvents;
    }

    /**
     * Filters the internal element tracking map to isolate and retrieve all child {@link Overlay} instances.
     * @return A linked list containing all {@link Overlay}s registered to this container.
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
     * Filters the internal element tracking map to isolate and retrieve all sub-containers.
     *
     * @return A linked list containing all nested {@link Container}s registered to this container.
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
     * Filters the internal element tracking map to isolate and retrieve all assigned {@link Layout} handlers.
     *
     * @return A linked list containing all {@link Layout} instances assigned to this container.
     */
    public LinkedList<Layout> getLayouts() {
        LinkedList<Layout> toReturn = new LinkedList<>();
        getElements().values().stream().filter(element -> element instanceof Layout).forEach(element -> {
            Layout layout = (Layout) element;
            toReturn.add(layout);
        });
        return toReturn;
    }

    /**
     * Appends a local CSS stylesheet to the container's styling context.
     * The provided file is automatically resolved into a compliant external URL format for JavaFX.
     *
     * @param file The local {@link File} object referencing the `.css` stylesheet.
     * @throws RuntimeException If the file cannot be resolved into a structurally valid URL.
     */
    public void addStyleSheet(File file) {
        try {
            stylesheets.add(file.toURI().toURL().toExternalForm());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves the collection of mapped CSS stylesheet URLs bound to this container.
     *
     * @return A list of formatted URL strings pointing to the stylesheets.
     */
    public List<String> getStylesheets() {
        return stylesheets;
    }

    /**
     * Compiles and assembles the container, mapping the engine's logical API constraints
     * onto the native JavaFX scene graph structure. Subclasses dictate exactly how child nodes
     * are structurally arranged.
     *
     * @return The constructed JavaFX {@link Node} representing this container and its fully populated children.
     */
    public abstract Node build();
}