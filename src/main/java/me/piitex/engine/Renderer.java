package me.piitex.engine;

import javafx.scene.Node;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import me.piitex.engine.hanlders.events.ContainerRenderEvent;
import me.piitex.engine.hanlders.events.LayoutRenderEvent;
import me.piitex.engine.layouts.Layout;
import me.piitex.engine.overlays.Overlay;

import java.util.*;

/**
 * An element which handles rendering of {@link Container}, {@link Layout}, and {@link Overlay}
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
    private String style;

    private List<String> styles = new ArrayList<>();
    private final Map<String, Node> renderedNodes = new HashMap<>();

    public Map<String, Node> getRenderedNodes() {
        return renderedNodes;
    }

    public Node getNode(String id) {
        return renderedNodes.get(id);
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getPrefWidth() {
        return prefWidth;
    }

    public double getPrefHeight() {
        return prefHeight;
    }

    public void setPrefSize(double width, double height) {
        this.width = width;
        this.height = height;
    }

    public double getMaxWidth() {
        return maxWidth;
    }

    public double getMaxHeight() {
        return maxHeight;
    }

    public void setMaxSize(double width, double height) {
        this.maxWidth = width;
        this.maxHeight = height;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    public double getBorderWidth() {
        return borderWidth;
    }

    public void setBorderWidth(double borderWidth) {
        this.borderWidth = borderWidth;
    }

    public List<String> getStyles() {
        return styles;
    }

    public void addStyle(String style) {
        styles.add(style);
    }

    public TreeMap<Integer, Element> getElements() {
        return elements;
    }

    /**
     * Retrieves the current element at the specific index. If the element is not present this will return null.
     * @param index Position of the desired element.
     * @return The {@link Element}
     */
    public Element getElementAt(int index) {
        return elements.get(index);
    }

    /**
     * Adds an element to the container. The added element will be indexed to the front of the container.
     * @param element The {@link Element} to be added.
     */
    public void addElement(Element element) {
        int index = element.getIndex();
        if (index == 0) {
            index = elements.size();
            element.setIndex(index);
        }

        addElement(element, index);
    }

    /**
     * Adds the element to the specific index. If there is an element already bound to that index it is shuffled forward.
     *
     * @param element The {@link Element} to add to the container.
     * @param index The index/order of the element.
     */
    public void addElement(Element element, int index) {
        Element current = elements.get(index);
        if (current != null) {
            int i = index + 1;
            addElement(getElementAt(index), i);
        }
        elements.put(index, element);
    }

    /**
     * Adds an array of elements to the container. The elements are positioned by the order of the array.
     * The added elements will be indexed to the front of the container.
     * @param elements The array of {@link Element}s to be added.
     */
    public void addElements(Element... elements) {
        for (Element element : elements) {
            addElement(element);
        }
    }

    /**
     * Adds a {@link LinkedList <Element>} of elements to the container. The elements are position by the order of the list.
     * @param elements The list of elements to be added.
     */
    public void addElements(LinkedList<Element> elements) {
        for (Element element : elements) {
            addElement(element);
        }
    }

    public void removeElement(int index) {
        elements.remove(index);
    }

    public void removeElement(Element element) {
        elements.remove(element.getIndex());
    }

    public void removeAllElement(Element element) {
        LinkedHashMap<Integer, Element> toRemove = new LinkedHashMap<>(elements);
        toRemove.forEach((integer, e) -> {
            if (e == element) {
                elements.remove(integer);
            }
        });
    }

    public void moveElement(int oldIndex, int newIndex) {
        Element element = elements.get(oldIndex);
        if (element != null) {
            elements.put(newIndex, element);
            elements.remove(oldIndex);
        }
    }

    public void removeAllElements() {
        elements.clear();
    }

    public double getOffsetX() {
        return xOffset;
    }

    public void setOffsetX(double xOffset) {
        this.xOffset = xOffset;
    }

    public double getOffsetY() {
        return yOffset;
    }

    public void setOffsetY(double yOffset) {
        this.yOffset = yOffset;
    }

    /* Generic methods */
    public LinkedList<Node> buildBase() {
        LinkedList<Node> toReturn = new LinkedList<>();
        for (Element element : getElements().values()) {
            Node node = null;
            if (element instanceof Overlay overlay) {
                node = overlay.render();
                node.getStyleClass().addAll(overlay.getStyles());
                overlay.setInputControls(node);
                overlay.setNode(node);
            }
            if (element instanceof Layout layout) {
                node = layout.render();
                LayoutRenderEvent event = new LayoutRenderEvent(layout.getPane(), layout);
                layout.getRenderEvents().forEach(iLayoutRender -> iLayoutRender.onLayoutRender(event));
                node.getStyleClass().addAll(layout.getStyles());
            }
            if (node != null) {
                updateOffsets(node);
                toReturn.add(node);
                if (element.getId() != null && !element.getId().isEmpty()) {
                    // Add the compiled nodes into the container
                    getRenderedNodes().put(element.getId(), node);

                }
            }

            // Render sub-containers last
            if (element instanceof Container container) {
                Map.Entry<Node, LinkedList<Node>> entry = container.build();
                toReturn.add(entry.getKey());
                container.setView(entry.getKey());
                entry.getKey().getStyleClass().addAll(container.getStyles());

                if (container.getId() != null && !container.getId().isEmpty()) {
                    getRenderedNodes().put(container.getId(), entry.getKey());
                }

                ContainerRenderEvent event = new ContainerRenderEvent(container, entry.getKey());
                container.getRenderEvents().forEach(iContainerRender -> iContainerRender.onContainerRender(event));

            }
        }

        return toReturn;
    }


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

    private void updateOffsets(Node node) {
        if (getOffsetX() > 0 || getOffsetY() > 0) {
            node.setTranslateX(node.getTranslateX() + getOffsetX());
            node.setTranslateY(node.getTranslateY() + getOffsetY());
        }
    }

    // Helper css function
    private String cssColor(Color color) {
        return String.format("rgba(%d, %d, %d, %f)",
                (int) (255 * color.getRed()),
                (int) (255 * color.getGreen()),
                (int) (255 * color.getBlue()),
                color.getOpacity());
    }


}
