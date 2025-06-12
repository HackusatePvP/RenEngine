package me.piitex.engine.layouts;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import me.piitex.engine.Container;
import me.piitex.engine.Element;
import me.piitex.engine.layouts.handles.ILayoutClickEvent;
import me.piitex.engine.overlays.Overlay;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

public abstract class Layout extends Element {
    private final Pane pane;
    private double x, y;
    private final double width, height;
    private final LinkedHashMap<Integer, Element> elements = new LinkedHashMap<>();
    private Pos alignment;
    private Color background;
    private double xOffset = 0, yOffset = 0;
    private ILayoutClickEvent clickEvent;

    protected Layout(Pane pane, double width, double height) {
        this.pane = pane;
        this.width = width;
        this.height = height;
    }

    public ILayoutClickEvent getClickEvent() {
        return clickEvent;
    }

    public void setClickEvent(ILayoutClickEvent clickEvent) {
        this.clickEvent = clickEvent;
    }

    public Pane getPane() {
        return pane;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public LinkedHashMap<Integer, Element> getElements() {
        return elements;
    }

    public void addElement(Element element) {
        elements.put(elements.size(), element);
    }

    public void addElement(Element element, int index) {
        Element current = elements.get(index);
        if (current != null) {
            int i = index + 1;
            addElement(elements.get(index), i);
        }
        elements.put(index, element);
    }

    public Pos getAlignment() {
        return alignment;
    }

    public void setAlignment(Pos alignment) {
        this.alignment = alignment;
    }

    public Color getBackground() {
        return background;
    }

    public void setBackground(Color background) {
        this.background = background;
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

    public abstract Pane render(Container container);

    public LinkedList<Node> buildBase(Container container) {
        LinkedList<Node> toReturn = new LinkedList<>();
        for (Element element : elements.values()) {
            Node node = null;
            if (element instanceof Overlay overlay) {
                node = overlay.render();
            }
            if (element instanceof Layout layout) {
                node = layout.render(container);
            }
            if (node != null) {
                updateOffsets(node);
                toReturn.add(node);
            }

            // Render sub-containers last
            if (element instanceof Container c) {
                Map.Entry<Node, LinkedList<Node>> entry = c.build();
                toReturn.add(entry.getKey());
            }
        }

        return toReturn;
    }

    private void updateOffsets(Node node) {
        if (getOffsetX() > 0 || getOffsetY() > 0) {
            node.setTranslateX(node.getTranslateX() + getOffsetX());
            node.setTranslateY(node.getTranslateY() + getOffsetY());
        }
    }
}