package me.piitex.engine.layouts;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.Pane;
import me.piitex.engine.Renderer;
import me.piitex.engine.layouts.handles.ILayoutClickEvent;
import me.piitex.engine.layouts.handles.ILayoutRender;

import java.util.ArrayList;
import java.util.List;

public abstract class Layout extends Renderer {
    private final Pane pane;
    private double x, y;
    private Insets padding;
    private Pos alignment;
    private ILayoutClickEvent clickEvent;
    private final List<ILayoutRender> renderEvents;

    protected Layout(Pane pane, double width, double height) {
        this.pane = pane;
        setWidth(width);
        setHeight(height);
        this.renderEvents = new ArrayList<>();
    }

    public ILayoutClickEvent getClickEvent() {
        return clickEvent;
    }

    public void setClickEvent(ILayoutClickEvent clickEvent) {
        this.clickEvent = clickEvent;
    }

    public void addRenderEvent(ILayoutRender renderEvent) {
        if (renderEvent != null) {
            this.renderEvents.add(renderEvent);
        }
    }

    public void removeRenderEvent(ILayoutRender renderEvent) {
        if (renderEvent != null) {
            this.renderEvents.remove(renderEvent);
        }
    }

    public List<ILayoutRender> getRenderEvents() {
        return renderEvents;
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