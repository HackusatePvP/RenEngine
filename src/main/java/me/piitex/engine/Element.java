package me.piitex.engine;

import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.Region;
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

import javax.annotation.Nonnull;
import java.util.function.Consumer;

/**
 * Represents the foundational graphical element that can be rendered inside a {@link Renderer}
 * (which includes {@link Container} and {@link Layout}).
 * <p>
 * This abstract base class provides the core lifecycle, state management, and event handling for all
 * renderable components within the GUI framework. It acts as a bridge between the engine's logical
 * representations and the underlying JavaFX {@link Node}.
 * </p>
 * <p>
 * Elements are heavily dependent on their rendering index (Z-order). A lower index dictates that the
 * element is drawn earlier, placing it underneath elements with a higher index.
 * </p>
 *
 * @see Renderer
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
     * Retrieves the rendering index (Z-order) of this element.
     *
     * @return The rendering index. A lower value indicates the element is rendered earlier (closer to the background).
     * An index of 0 indicates automatic assignment by the engine. The lowest manual layer should generally be 1.
     */
    public int getIndex() {
        return index;
    }

    /**
     * Sets the rendering index (Z-order) of this element.
     * <p>
     * Modifying this value allows you to control the depth of the element on the screen. Higher index values
     * will render the element on top of those with lower indices. Set to 0 to let the engine automatically
     * assign the index based on insertion order.
     * </p>
     *
     * @param index The new rendering index for the element.
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * Toggles the interactive state of the element.
     * <p>
     * When disabled, the underlying JavaFX node will no longer process user inputs or events.
     * If the underlying {@link Node} has not been assembled or set yet, this will log an error
     * rather than crashing the application.
     * </p>
     *
     * @param enabled {@code true} to enable interactions, {@code false} to disable them.
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (getNode() != null) {
            getNode().setDisable(!enabled);
        } else {
            NodeNotDefinedException exception = new NodeNotDefinedException(this);
            logger.error(exception.getMessage(), exception);
        }
    }

    /**
     * Checks if the element is currently enabled for user interaction.
     *
     * @return {@code true} if the element is enabled, {@code false} otherwise.
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Retrieves the cached JavaFX {@link Node} associated with this element.
     * <p>
     * This cache is typically populated when the engine invokes the {@link #assemble()} method.
     * If the element has not been assembled yet, this method will return null.
     * </p>
     *
     * @return The cached JavaFX node if present, {@code null} otherwise.
     */
    @Nullable
    public Node getNode() {
        return node;
    }

    /**
     * Manually assigns the underlying JavaFX {@link Node} for this element.
     *
     * @param node The JavaFX node to associate with this engine element.
     */
    public void setNode(@Nonnull Node node) {
        this.node = node;
    }

    /**
     * Retrieves the currently assigned mouse cursor for this element.
     *
     * @return The JavaFX {@link Cursor} applied to this element when hovered, or {@code null} if default.
     */
    public Cursor getCursor() {
        return cursor;
    }

    /**
     * Disables all JavaFX style sheets including defaults. The JavaFX {@link Node} will need to be assembled first for this method to be effective.
     */
    public void removeStyles() {
        if (getNode() != null) {
            getNode().getStyleClass().clear();
        } else {
            logger.error("Node must be assembled to remove styles!", new UnsupportedOperationException());
        }
    }

    public void setBorder(Border border) {
        if (getNode() instanceof Region region) {
            region.setBorder(border);
        } else {
            logger.error("Node must be an implementation of Region.", new UnsupportedOperationException());
        }
    }

    public void setBackground(Background background) {
        if (getNode() instanceof Region region) {
            region.setBackground(background);
        } else {
            logger.error("Node must be an implementation of Region.", new UnsupportedOperationException());
        }
    }

    /**
     * Sets a specific mouse cursor to display when the user hovers over this element.
     * <p>
     * This automatically applies the cursor to the underlying JavaFX {@link Node} if it is currently cached.
     * </p>
     *
     * @param cursor The JavaFX {@link Cursor} to display.
     */
    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
        if (node != null) {
            node.setCursor(cursor);
        }
    }

    /**
     * Registers a callback to be fired when the user clicks on this element.
     * <p>
     * This wraps the native JavaFX {@link MouseEvent#MOUSE_CLICKED} event into a framework-specific {@link ElementClickEvent}.
     * </p>
     *
     * @param clickConsumer The consumer to accept the click event logic.
     */
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

    /**
     * Registers a callback to be fired when the user releases a mouse click on this element.
     * <p>
     * This wraps the native JavaFX {@link MouseEvent#MOUSE_RELEASED} event into a framework-specific {@link ElementClickReleaseEvent}.
     * </p>
     *
     * @param clickReleaseConsumer The consumer to accept the click release event logic.
     */
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

    /**
     * Registers a callback to be fired when the user's mouse pointer enters the bounds of this element.
     * <p>
     * This wraps the native JavaFX {@link MouseEvent#MOUSE_ENTERED} event into a framework-specific {@link ElementHoverEvent}.
     * </p>
     *
     * @param hoverConsumer The consumer to accept the hover event logic.
     */
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

    /**
     * Registers a callback to be fired when the user's mouse pointer exits the bounds of this element.
     * <p>
     * This wraps the native JavaFX {@link MouseEvent#MOUSE_EXITED} event into a framework-specific {@link ElementExitEvent}.
     * </p>
     *
     * @param mouseExitConsumer The consumer to accept the mouse exit event logic.
     */
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
     * Retrieves the registered click event consumer.
     * @return The consumer handling click events, or {@code null} if none is set.
     */
    public Consumer<ElementClickEvent> getOnClick() {
        return clickConsumer;
    }

    /**
     * Retrieves the registered click release event consumer.
     * @return The consumer handling click release events, or {@code null} if none is set.
     */
    public Consumer<ElementClickReleaseEvent> getOnRelease() {
        return clickReleaseConsumer;
    }

    /**
     * Retrieves the registered mouse exit event consumer.
     * @return The consumer handling mouse exit events, or {@code null} if none is set.
     */
    public Consumer<ElementExitEvent> getOnMouseExit() {
        return mouseExitConsumer;
    }

    /**
     * Retrieves the registered mouse hover event consumer.
     * @return The consumer handling mouse hover events, or {@code null} if none is set.
     */
    public Consumer<ElementHoverEvent> getOnHover() {
        return hoverConsumer;
    }

    /**
     * Compiles and constructs the engine element into a standard JavaFX {@link Node}.
     * <p>
     * This method is called by the engine during the rendering phase to translate custom elements
     * into the native scene graph.
     * </p>
     * <ul>
     * <li>For {@link Overlay} instances, this invokes their specific rendering functions (e.g., {@link TextOverlay#render()}).</li>
     * <li>For {@link Layout} instances, this invokes {@link Layout#render()}.</li>
     * <li>For {@link Container} instances, this invokes {@link Container#build()}.</li>
     * </ul>
     *
     * @return The fully constructed JavaFX node ready for the scene graph.
     */
    public abstract Node assemble();
}