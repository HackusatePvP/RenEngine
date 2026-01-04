package me.piitex.engine;


import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import me.piitex.engine.containers.Container;
import me.piitex.engine.exceptions.NodeNotDefinedException;
import me.piitex.engine.hanlders.events.*;
import me.piitex.engine.layouts.Layout;
import me.piitex.engine.overlays.ImageOverlay;
import me.piitex.engine.overlays.Overlay;
import me.piitex.engine.overlays.TextOverlay;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

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
    private boolean enabled = true;
    @Nullable private Node node; // Underlying JavaFX component
    private Cursor cursor;
    private Consumer<ElementHoverEvent> hoverConsumer;
    private Consumer<ElementClickEvent> clickConsumer;
    private Consumer<ElementExitEvent> mouseExitConsumer;
    private Consumer<ElementClickReleaseEvent> clickReleaseConsumer;

    private static final Logger logger = LoggerFactory.getLogger(Element.class);

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

    public Cursor getCursor() {
        return cursor;
    }

    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
        if (node != null) {
            node.setCursor(cursor);
        }
    }

    public void onClick(Consumer<ElementClickEvent> clickConsumer) {
        this.clickConsumer = clickConsumer;

        if (node != null) {
            if (clickConsumer != null) {
                node.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
                    clickConsumer.accept(new ElementClickEvent(this, mouseEvent, mouseEvent.getSceneX(), mouseEvent.getSceneY()));
                });
            }
        }
    }

    public void onClickRelease(Consumer<ElementClickReleaseEvent> clickReleaseConsumer) {
        this.clickReleaseConsumer = clickReleaseConsumer;

        if (node != null) {
            if (clickReleaseConsumer != null) {
                node.addEventHandler(MouseEvent.MOUSE_RELEASED, mouseEvent -> {
                    clickReleaseConsumer.accept(new ElementClickReleaseEvent(this, mouseEvent));
                });
            }
        }
    }

    public void onHover(Consumer<ElementHoverEvent> hoverConsumer) {
        this.hoverConsumer = hoverConsumer;

        if (node != null) {
            if (hoverConsumer != null) {
                node.addEventHandler(MouseEvent.MOUSE_ENTERED, mouseEvent -> {
                    hoverConsumer.accept(new ElementHoverEvent(this, mouseEvent));
                });
            }
        }
    }

    public void onMouseExit(Consumer<ElementExitEvent> mouseExitConsumer) {
        this.mouseExitConsumer = mouseExitConsumer;

        if (node != null) {
            if (mouseExitConsumer != null) {
                node.addEventHandler(MouseEvent.MOUSE_EXITED, mouseEvent -> {
                    mouseExitConsumer.accept(new ElementExitEvent(this, mouseEvent));
                });
            }
        }
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
