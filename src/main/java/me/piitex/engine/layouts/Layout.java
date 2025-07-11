package me.piitex.engine.layouts;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.Pane;
import me.piitex.engine.Renderer;
import me.piitex.engine.layouts.handles.ILayoutClickEvent;
import me.piitex.engine.layouts.handles.ILayoutRender;

public abstract class Layout extends Renderer {
    private final Pane pane;
    private double x, y;
    private Insets padding;
    private Pos alignment;
    private ILayoutClickEvent clickEvent;
    private ILayoutRender renderEvent;

    protected Layout(Pane pane, double width, double height) {
        this.pane = pane;
        setWidth(width);
        setHeight(height);
    }

    public ILayoutClickEvent getClickEvent() {
        return clickEvent;
    }

    public void setClickEvent(ILayoutClickEvent clickEvent) {
        this.clickEvent = clickEvent;
    }

    public ILayoutRender getRenderEvent() {
        return renderEvent;
    }

    public void onRender(ILayoutRender renderEvent) {
        this.renderEvent = renderEvent;
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

    public Pos getAlignment() {
        return alignment;
    }

    public void setAlignment(Pos alignment) {
        this.alignment = alignment;
    }

    public void setPadding(Insets padding) {
        this.padding = padding;
    }

    public Insets getPadding() {
        return padding;
    }

    public abstract Pane render();
}