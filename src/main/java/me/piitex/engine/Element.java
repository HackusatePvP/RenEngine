package me.piitex.engine;


import javafx.scene.Node;
import me.piitex.engine.exceptions.NodeNotDefinedException;
import me.piitex.engine.layouts.Layout;
import me.piitex.engine.overlays.ImageOverlay;
import me.piitex.engine.overlays.Overlay;
import me.piitex.engine.overlays.TextOverlay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a graphical element that can be rendered to the {@link Window} or a {@link Container}.
 * <p>
 * This is an abstract base class for all renderable elements in the GUI framework.
 * Elements are organized by their rendering index, which determines the order in which
 * they are drawn. A lower index means the element will be rendered earlier (underneath others).
 * </p>
 *
 * @see Container
 * @see Overlay
 * @see Layout
 */
public abstract class Element {
    private int index = 0;
    private String id;
    private boolean enabled = true;
    private Node node; // Underlying JavaFX component

    private static final Logger logger = LoggerFactory.getLogger(Element.class);

    public String getId() {
        return id;
    }

    /**
     * Sets an identifier for the element. Used to fetch the compiled {@link javafx.scene.Node} after rendering.
     * @param id Unique identifier.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Retrieves the rendering index of this element.
     *
     * @return The rendering index of the element. A lower value means the element is rendered earlier. An index of 0 results in automatic assignment. Use '1' as the lowest layer.
     */
    public int getIndex() {
        return index;
    }

    /**
     * Sets the rendering index of this element.
     * <p>
     * An index of 1 will cause the element to be rendered first (at the bottom layer).
     * Higher index values will render the element on top of those with lower indices. An index of 0 results in automatic assignment.
     * </p>
     *
     * @param index The new rendering index for the element.
     */
    public void setIndex(int index) {
        this.index = index;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (getNode() != null) {
            getNode().setDisable(!enabled);
        } else {
            NodeNotDefinedException exception = new NodeNotDefinedException(this);
            logger.error(exception.getMessage(), exception);
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    /**
     * Assembles the element into its JavaFX {@link Node}.
     *
     * <p>
     *     For {@link Overlay}'s it will return the render functions. Examples: {@link TextOverlay#render()}, {@link ImageOverlay#render()}
     * </p>
     * <p>
     *     For {@link Layout}'s it will return the {@link Layout#render()} result.
     * </p>
     * <p>
     *     For {@link Container}'s it will return the {@link Container#build()} result.
     * </p>
     * @return The constructed node.
     */
    public abstract Node assemble();
}
