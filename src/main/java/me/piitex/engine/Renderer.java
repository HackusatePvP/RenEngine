package me.piitex.engine;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import me.piitex.engine.containers.Container;
import me.piitex.engine.hanlders.IRendererKey;
import me.piitex.engine.hanlders.events.ContainerRenderEvent;
import me.piitex.engine.hanlders.events.KeyPressEvent;
import me.piitex.engine.hanlders.events.LayoutRenderEvent;
import me.piitex.engine.layouts.Layout;
import me.piitex.engine.overlays.Overlay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.*;

/**
 * An element which handles the core visual rendering and structural management of child {@link Element}s.
 * <p>
 * This class acts as the bridge between the engine's logical element hierarchy and the native
 * JavaFX scene graph. It manages dimensions, styling, and nested elements via a z-index mapping.
 * Subclasses like {@link Container} and {@link Layout} inherit this capability to manage
 * their own distinct collections of children.
 * </p>
 *
 * @see Layout
 * @see Container
 */
public class Renderer extends Element {
    private final TreeMap<Integer, Element> elements = new TreeMap<>();
    private double width, height;
    private double prefWidth, prefHeight;
    private double maxWidth, maxHeight;
    private double xOffset = 0, yOffset = 0;
    private Color backgroundColor;
    private Color borderColor;
    private double borderWidth = 1;
    private final List<String> styles = new ArrayList<>();
    private Window window;
    private final Properties properties = new Properties();

    private static final Logger logger = LoggerFactory.getLogger(Renderer.class);

    // Handlers for events
    private IRendererKey iRendererKey;

    /**
     * Retrieves the custom key press event handler bound to this renderer.
     *
     * @return The active {@link IRendererKey} implementation, or null if none is set.
     */
    public IRendererKey getiRendererKey() {
        return iRendererKey;
    }

    /**
     * Binds a custom key press event handler to this renderer's underlying JavaFX node.
     * The event will be attached during the {@link #assemble()} phase.
     *
     * @param iRendererKey The handler logic to execute on key press events.
     */
    public void onKeyPress(IRendererKey iRendererKey) {
        this.iRendererKey = iRendererKey;
    }

    /**
     * Retrieves the currently configured minimum width of the renderer.
     *
     * @return The width constraint in pixels.
     */
    public double getWidth() {
        return width;
    }

    /**
     * Sets the minimum width of the renderer and directly applies it to the underlying JavaFX Region.
     *
     * @param width The targeted width in pixels.
     */
    public void setWidth(double width) {
        this.width = width;
        if (getNode() instanceof Region region) {
            region.setMinWidth(width);
        }
    }

    /**
     * Retrieves the currently configured minimum height of the renderer.
     *
     * @return The height constraint in pixels.
     */
    public double getHeight() {
        return height;
    }

    /**
     * Sets the minimum height of the renderer and directly applies it to the underlying JavaFX Region.
     *
     * @param height The targeted height in pixels.
     */
    public void setHeight(double height) {
        this.height = height;

        if (getNode() instanceof Region region) {
            region.setMinHeight(height);
        }
    }

    /**
     * Retrieves the preferred layout width of the renderer.
     *
     * @return The preferred width in pixels.
     */
    public double getPrefWidth() {
        return prefWidth;
    }

    /**
     * Retrieves the preferred layout height of the renderer.
     *
     * @return The preferred height in pixels.
     */
    public double getPrefHeight() {
        return prefHeight;
    }

    /**
     * Sets the preferred layout width of the renderer. Used by layout managers to compute sizes.
     *
     * @param prefWidth The targeted preferred width.
     */
    public void setPrefWidth(double prefWidth) {
        this.prefWidth = prefWidth;

        if (getNode() instanceof Region region) {
            region.setPrefWidth(height);
        }
    }

    /**
     * Sets the preferred layout height of the renderer. Used by layout managers to compute sizes.
     *
     * @param prefHeight The targeted preferred height.
     */
    public void setPrefHeight(double prefHeight) {
        this.prefHeight = prefHeight;

        if (getNode() instanceof Region region) {
            region.setPrefHeight(height);
        }
    }

    /**
     * Sets both the preferred layout width and height of the renderer simultaneously.
     *
     * @param width  The targeted preferred width.
     * @param height The targeted preferred height.
     */
    public void setPrefSize(double width, double height) {
        this.prefWidth = width;
        this.prefHeight = height;

        if (getNode() instanceof Region region) {
            region.setPrefSize(width, height);
        }
    }

    /**
     * Retrieves the maximum allowed width of the renderer constraint.
     *
     * @return The max width in pixels.
     */
    public double getMaxWidth() {
        return maxWidth;
    }

    /**
     * Retrieves the maximum allowed height of the renderer constraint.
     *
     * @return The max height in pixels.
     */
    public double getMaxHeight() {
        return maxHeight;
    }

    /**
     * Sets the absolute maximum dimensions this renderer is allowed to scale to.
     *
     * @param width  The maximum width ceiling.
     * @param height The maximum height ceiling.
     */
    public void setMaxSize(double width, double height) {
        this.maxWidth = width;
        this.maxHeight = height;

        if (getNode() instanceof Region region) {
            region.setMaxSize(width, height);
        }
    }

    /**
     * Retrieves the assigned background color logic.
     *
     * @return The JavaFX {@link Color} assigned as the background.
     */
    public Color getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * Sets the background color. To visually update the node, {@link #setStyling(Node)} must be invoked
     * during or after assembly.
     *
     * @param backgroundColor The JavaFX {@link Color} to apply.
     */
    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;

        if (Platform.isFxApplicationThread() && getNode() instanceof Pane pane) {
            pane.setBackground(new Background(new BackgroundFill(getBackgroundColor(), CornerRadii.EMPTY, Insets.EMPTY)));
        }
    }

    /**
     * Retrieves the assigned border color logic.
     *
     * @return The JavaFX {@link Color} assigned as the border stroke.
     */
    public Color getBorderColor() {
        return borderColor;
    }

    /**
     * Sets the border color. To visually update the node, {@link #setStyling(Node)} must be invoked
     * during or after assembly.
     *
     * @param borderColor The JavaFX {@link Color} to apply.
     */
    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;

        if (Platform.isFxApplicationThread() && getNode() instanceof Pane pane) {
            pane.setBorder(new Border(new BorderStroke(borderColor, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        }
    }

    /**
     * Retrieves the stroke width of the renderer's border constraint.
     *
     * @return The border thickness.
     */
    public double getBorderWidth() {
        return borderWidth;
    }

    /**
     * Sets the uniform stroke thickness for the element's border.
     *
     * @param borderWidth The desired border thickness in pixels.
     */
    public void setBorderWidth(double borderWidth) {
        this.borderWidth = borderWidth;
    }

    /**
     * Retrieves the list of native JavaFX CSS style classes assigned to this renderer.
     *
     * @return A list of CSS class names.
     */
    public List<String> getStyles() {
        return styles;
    }

    /**
     * Appends a new JavaFX CSS class to the renderer and binds it to the current native node immediately.
     *
     * @param style The CSS class string representation.
     */
    public void addStyle(String style) {
        styles.add(style);
        getNode().getStyleClass().add(style);
    }

    /**
     * Retrieves the top-level application Window context hosting this renderer.
     *
     * @return The parent {@link Window}.
     */
    public Window getWindow() {
        return window;
    }

    /**
     * Binds this renderer to a specific top-level application Window context.
     *
     * @param window The {@link Window} instance to associate with.
     */
    public void setWindow(Window window) {
        this.window = window;
    }

    /**
     * Retrieves the mapped hierarchy of all child elements tracked by this renderer.
     * Sorted inherently by their assigned index layout order.
     *
     * @return A TreeMap mapping integer indices to {@link Element} objects.
     */
    public TreeMap<Integer, Element> getElements() {
        return elements;
    }

    /**
     * Retrieves the child element structurally positioned at a specific z-index.
     *
     * @param index The positional index to search for.
     * @return The matching {@link Element}, or null if no element occupies that index.
     */
    @Nullable
    public Element getElementAt(int index) {
        return elements.get(index);
    }

    /**
     * Retrieves the child element situated at the lowest assigned index constraint (bottom layer).
     *
     * @return The lowest indexed {@link Element}.
     * @throws NoSuchElementException if the element map is empty.
     */
    public Element getFirstElement() {
        return elements.firstEntry().getValue();
    }

    /**
     * Retrieves the child element situated at the highest assigned index constraint (top layer).
     *
     * @return The highest indexed {@link Element}.
     * @throws NoSuchElementException if the element map is empty.
     */
    public Element getLastElement() {
        return elements.lastEntry().getValue();
    }

    /**
     * Adds the {@link Element} to this renderer (eg, {@link Container}, or {@link Layout}).
     * <p>
     * Calculates the next available logical index (or uses its configured intrinsic index),
     * maps the element, and immediately compiles it for the screen.
     * <p>
     * <b>Threading Constraint:</b> This method immediately manipulates the live JavaFX scene graph
     * and <b>must</b> be executed on the JavaFX Application Thread. If you are dispatching this
     * from a background worker thread, you must handle the synchronization manually
     * (e.g., via {@link javafx.application.Platform#runLater(Runnable)}).
     * </p>
     * <pre>
     * {@code
     *
     * // In application thread
     * Renderer view = ... // Obtain the renderer
     * Element element = ... // Initialize the element
     * view.addElement(element);
     *
     * // In background thread
     * new Thread(() -> {
     *     Renderer view = ... // Obtain the renderer
     *     Element element = ... // Initialize the element
     *
     *     // Synchronize the execution
     *     Platform.runLater(() -> {
     *         view.addElement(element);
     *     });
     * })
     * }
     * </pre>
     *
     * @param element The {@link Element} component to append and render.
     */
    public void addElement(Element element) {
        if (element == this) {
            throw new UnsupportedOperationException("Cannot add parent as element!");
        }

        int index = element.getIndex();
        if (index == 0) {
            index = elements.size();
        }

        addElement(element, index);
    }

    /**
     * Injects an element into a specific z-index layout slot and forcefully renders it to the screen.
     * <p>
     * <b>Threading Constraint:</b> This is a mutating operation that recursively shifts existing
     * elements and immediately pushes the newly compiled {@link Node} into the live view layer.
     * To prevent an {@link IllegalStateException}, this <b>must</b> be invoked on the JavaFX
     * Application Thread. You can find more info in the base {@link #addElement(Element)} function.
     * </p>
     * <pre>
     * {@code
     * Renderer view = ...
     * Element element = ...
     *
     * // Inserts the element exactly at layer 5 and renders immediately
     * view.addElement(element, 5);
     * }
     * </pre>
     *
     * @param element The {@link Element} to push and render.
     * @param index   The mandatory z-index layout order integer.
     */
    public void addElement(Element element, int index) {
        if (element == this) {
            throw new UnsupportedOperationException("Cannot add parent as element!");
        }

        Element current = elements.get(index);
        if (current != null && current != element) {
            int i = index + 1;
            addElement(getElementAt(index), i);
            elements.remove(index);
        }
        element.setIndex(index);
        elements.put(index, element);

        Node node = element.assemble();
        element.setNode(node);

        addToView(node, index);
    }

    /**
     * Batch processes an array of elements, compiling and rendering each one sequentially.
     * <p>
     * <b>Threading Constraint:</b> Because this delegates to {@link #addElement(Element)},
     * all view manipulations occur instantly. This method <b>must</b> be executed on the
     * JavaFX Application Thread. You can find more info in the base {@link #addElement(Element)} function.
     * </p>
     * <pre>
     * {@code
     * Element e1 = ...
     * Element e2 = ...
     *
     * // Renders both elements sequentially
     * view.addElements(e1, e2);
     * }
     * </pre>
     *
     * @param elements The arbitrary array of {@link Element} components to append and render.
     */
    public void addElements(Element... elements) {
        for (Element element : elements) {
            if (element == this) {
                throw new UnsupportedOperationException("Cannot add parent as element!");
            }
            addElement(element);
        }
    }

    /**
     * Batch processes a linked list of elements, compiling and rendering each one sequentially.
     * <p>
     * <b>Threading Constraint:</b> Because this delegates to {@link #addElement(Element)},
     * all view manipulations occur instantly. This method <b>must</b> be executed on the
     * JavaFX Application Thread. You can find more info in the base {@link #addElement(Element)} function.
     * </p>
     * <pre>
     * {@code
     * LinkedList<Element> list = new LinkedList<>();
     * list.add(e1);
     * list.add(e2);
     *
     * // Renders the entire list sequentially
     * view.addElements(list);
     * }
     * </pre>
     *
     * @param elements The {@link LinkedList} of child elements to add and render.
     */
    public void addElements(LinkedList<Element> elements) {
        for (Element element : elements) {
            if (element == this) {
                throw new UnsupportedOperationException("Cannot add parent as element!");
            }
            addElement(element);
        }
    }

    /**
     * Identifies an element strictly by its stored z-index layout mapped key and entirely removes it
     * from both the structural map and the visual JavaFX node representation.
     *
     * @param index The positional mapping key to eliminate.
     */
    public void removeElement(int index) {
        removeElement(getElementAt(index));
    }

    /**
     * Explicitly unlinks a registered element from the structural index map and purges it
     * from the active JavaFX rendering tree.
     *
     * @param element The targeted {@link Element} to delete.
     */
    public void removeElement(Element element) {
        elements.remove(element.getIndex());
        removeFromView(element.getNode());
    }

    /**
     * Retrieves and purges the lowest-indexed (bottommost) element structure.
     */
    public void removeFirstElement() {
        removeElement(elements.firstKey());
    }

    /**
     * Retrieves and purges the highest-indexed (topmost) element structure.
     */
    public void removeLastElement() {
        removeElement(elements.lastKey());
    }

    /**
     * Iterates explicitly through the index map to eliminate every structural reference
     * tying matching a specific element instance. Safely handles bulk cleanup.
     *
     * @param element The targeted {@link Element} reference to scrub out completely.
     */
    public void removeAllElement(Element element) {
        LinkedHashMap<Integer, Element> toRemove = new LinkedHashMap<>(elements);
        toRemove.forEach((integer, e) -> {
            if (e == element) {
                removeElement(e);
            }
        });
    }

    /**
     * Relocates an already mapped element context from an existing indexed order
     * cleanly into a new indexed target position.
     * (Note: This mutates map logic but requires a re-render phase to push JavaFX Node hierarchy changes).
     *
     * @param oldIndex The index the element is currently sitting at.
     * @param newIndex The fresh designated mapping layout destination.
     */
    public void moveElement(int oldIndex, int newIndex) {
        Element element = elements.get(oldIndex);
        if (element != null) {
            elements.put(newIndex, element);
            elements.remove(oldIndex);
        }
    }

    /**
     * Performs a hard reset on the container system. Wipes all logical mappings
     * alongside actively stripping the children from the parent JavaFX Pane.
     */
    public void removeAllElements() {
        elements.clear();
        if (getNode() instanceof Pane pane) {
            pane.getChildren().clear();
        }
    }

    /**
     * Replaces an element located at a specific index mapping. Overwrites both the logic map
     * and strictly forces the underlying JavaFX hierarchy to compile and exchange view layers.
     *
     * @param index   The location index to intercept.
     * @param element The new replacement {@link Element}.
     */
    public void replaceElement(int index, Element element) {
        if (getNode() instanceof Pane pane) {
            pane.getChildren().remove(index);
            pane.getChildren().add(index, element.assemble());
            elements.replace(index, element);
        }
    }

    /**
     * Checks if an element resides structurally at the queried index coordinate.
     *
     * @param index The integer lookup key.
     * @return True if populated; false otherwise.
     */
    public boolean containsElement(int index) {
        return elements.containsKey(index);
    }

    /**
     * Verifies if a specific element reference is currently being tracked visually in the map.
     *
     * @param element The targeted element logic constraint.
     * @return True if the reference exists internally; false otherwise.
     */
    public boolean containsElement(Element element) {
        return elements.containsValue(element);
    }

    /**
     * The internal translation mechanism to bridge a compiled JavaFX Node straight into an active Pane.
     * Respects safe fallback logic if an index allocation fails or extends out of bounds, preventing index crashes.
     *
     * @param node  The fully compiled JavaFX visual component.
     * @param index The expected hierarchy rendering order priority.
     */
    public void addToView(Node node, int index) {
        if (getNode() instanceof Pane pane) {
            if (!pane.getChildren().contains(node)) {
                if (index >= 0 && index <= pane.getChildren().size()) {
                    pane.getChildren().add(index, node);
                } else {
                    // Fallback for an invalid index
                    pane.getChildren().add(node);
                }
                pane.requestLayout();
            } else {
                pane.getChildren().remove(node);

                // Check index again after removing it.
                if (index <= pane.getChildren().size()) {
                    logger.warn("Replacing '{}' with '{}'", index, node.getClass().toString());
                    pane.getChildren().add(index, node);
                } else {
                    // If the new index is out of bounds, add it to the end.
                    logger.debug("Could not allocate space. Node will be rendered first.");
                    pane.getChildren().add(node);
                }
                pane.requestLayout();
                logger.debug("Node already exists in renderer. Shuffling '{}' forward.", node.getClass().toString());
            }
        } else {
            logger.error("Invalid renderer type \"{}\"", getNode().toString(), new RuntimeException());
        }
    }

    /**
     * Strictly isolates the requested JavaFX Node structure and attempts to remove it
     * exclusively out of the target renderer's underlying visual scope layer.
     *
     * @param node The raw target element.
     */
    public void removeFromView(Node node) {
        if (getNode() instanceof Pane pane) {
            pane.getChildren().remove(node);
        }
    }

    /**
     * Retrieves the custom lateral x-coordinate translation displacement configured for rendering.
     *
     * @return The offset distance.
     */
    public double getOffsetX() {
        return xOffset;
    }

    /**
     * Injects a lateral translation layout buffer padding along the horizontal x-axis.
     *
     * @param xOffset The explicit translation distance modification.
     */
    public void setOffsetX(double xOffset) {
        this.xOffset = xOffset;
    }

    /**
     * Retrieves the custom vertical y-coordinate translation displacement configured for rendering.
     *
     * @return The offset distance.
     */
    public double getOffsetY() {
        return yOffset;
    }

    /**
     * Injects a vertical translation layout buffer padding along the vertical y-axis.
     *
     * @param yOffset The explicit translation distance modification.
     */
    public void setOffsetY(double yOffset) {
        this.yOffset = yOffset;
    }

    /**
     * Interprets internal renderer CSS lists, background fills, and border properties
     * compiling them out into inline raw CSS injections forcibly applied down upon the native JavaFX context node.
     *
     * @param node The physical target JavaFX node to mutate.
     */
    public void setStyling(Node node) {
        node.getStyleClass().addAll(styles);
        if (node instanceof Region region) {
            StringBuilder inLineCss = new StringBuilder();
            if (backgroundColor != null) {
                inLineCss.append("-fx-background-color: ").append(cssColor(backgroundColor)).append("; ");
            }

            if (borderColor != null) {
                inLineCss.append("-fx-border-color: ").append(cssColor(borderColor)).append("; ");
                inLineCss.append("-fx-border-width: ").append(borderWidth).append(" ").append(borderWidth).append(" ").append(borderWidth).append(" ").append(borderWidth).append("; ");
                inLineCss.append("-fx-border-style: ").append("solid").append("; ");
            }

            region.setStyle(inLineCss.toString());
        }
    }

    /**
     * Internal geometry translation handler enforcing horizontal and vertical custom offset constraints.
     *
     * @param node The rendered view.
     */
    private void updateOffsets(Node node) {
        if (getOffsetX() > 0 || getOffsetY() > 0) {
            node.setTranslateX(node.getTranslateX() + getOffsetX());
            node.setTranslateY(node.getTranslateY() + getOffsetY());
        }
    }

    /**
     * Converts a standard JavaFX Color object payload explicitly down into string-represented
     * raw RGBA formatting syntax used internally by CSS parsers.
     *
     * @param color The incoming JavaFX color space requirement.
     * @return A formatted `-fx-` compatible RGBA format string block.
     */
    private String cssColor(Color color) {
        return String.format("rgba(%d, %d, %d, %f)",
                (int) (255 * color.getRed()),
                (int) (255 * color.getGreen()),
                (int) (255 * color.getBlue()),
                color.getOpacity());
    }

    /**
     * The pivotal engine processing cycle. This iterates over the underlying logical structures
     * (Layouts and Containers), handles structural translation, resolves nested hierarchy lists dynamically,
     * builds and integrates keyboard logic routing, and ultimately fires local render listener chains cleanly.
     *
     * @return The freshly constructed and compiled primary structural JavaFX node.
     */
    @Override
    public Node assemble() {
        Node node = null;
        if (this instanceof Container container) {
            node = container.build();
        }
        if (this instanceof Layout layout) {
            node = layout.render();
        }

        if (node != null && iRendererKey != null) {
            node.setOnKeyPressed(event -> {
                iRendererKey.onKeyPress(new KeyPressEvent(event));
            });
        }

        // Assemble existing elements.
        if (node instanceof Pane pane) {
            for (Element element : getElements().values()) {
                Node child = element.assemble();
                if (element instanceof Overlay overlay) {
                    overlay.setNode(child);
                }
                if (!pane.getChildren().contains(child)) {
                    pane.getChildren().add(child);
                }
            }
        }

        // Handle events
        if (this instanceof Container container) {
            ContainerRenderEvent event = new ContainerRenderEvent(container, node);
            container.getRenderEvents().forEach(iContainerRender -> iContainerRender.onContainerRender(event));
        }
        if (this instanceof Layout layout) {
            LayoutRenderEvent event = new LayoutRenderEvent(layout.getPane(), layout);
            layout.getRenderEvents().forEach(iLayoutRender -> iLayoutRender.onLayoutRender(event));
        }

        return node;
    }

    /**
     * Binds custom metadata properties natively onto the element footprint scope tracking memory.
     *
     * @param key  The mapping string definition identifier.
     * @param data The payload definition constraint.
     */
    public void addProperties(String key, String data) {
        properties.setProperty(key, data);
    }

    /**
     * Extracts a locally bound string-formatted data value linked by key.
     *
     * @param key The mapped lookup token to hunt down.
     * @return The string data payload retrieved, or null if unassigned.
     */
    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    /**
     * Verifies if specific logic or metadata string flags have been configured physically locally onto this exact element instance block scope.
     *
     * @param key The defined identification key string constraint.
     * @return True if defined contextually within the engine mapping map scope; false otherwise.
     */
    public boolean hasProperty(String key) {
        return properties.containsKey(key);
    }
}