package me.piitex.engine.layouts;

import javafx.geometry.Pos;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import me.piitex.engine.Container;
import me.piitex.engine.Element;
import me.piitex.engine.layouts.handles.ILayoutClickEvent;
import java.util.LinkedHashMap;

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
            addElement(current, index + 1);
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
}