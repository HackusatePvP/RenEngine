package me.piitex.engine.layouts;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import me.piitex.engine.Container;
import me.piitex.engine.DisplayOrder;
import me.piitex.engine.overlays.Overlay;

import java.util.LinkedList;
import java.util.List;

public abstract class Layout {
    private final Pane pane;
    private double x, y;
    private final double width, height;
    private DisplayOrder order = DisplayOrder.LOW;
    private final LinkedList<Overlay> overlays = new LinkedList<>();
    private final LinkedList<Layout> childLayouts = new LinkedList<>();


    protected Layout(Pane pane, double width, double height) {
        this.pane = pane;
        this.width = width;
        this.height = height;
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

    public DisplayOrder getOrder() {
        return order;
    }

    public void setOrder(DisplayOrder order) {
        this.order = order;

        // Update order for all overlays
        for (Overlay overlay : getOverlays()) {
            overlay.setOrder(order);
        }

        for (Layout layout : getChildLayouts()) {
            layout.setOrder(order);
        }
    }

    public LinkedList<Overlay> getOverlays() {
        return overlays;
    }

    public void addOverlay(Overlay overlay) {
        this.overlays.add(overlay);
    }

    public void addOverlays(Overlay... overlays) {
        this.overlays.addAll(List.of(overlays));
    }

    public void addOverlays(List<Overlay> overlays) {
        this.overlays.addAll(overlays);
    }

    public LinkedList<Layout> getChildLayouts() {
        return childLayouts;
    }

    public void addChildLayout(Layout layout) {
        this.childLayouts.add(layout);
    }

    public void addChildLayouts(LinkedList<Layout> layouts) {
        this.childLayouts.addAll(layouts);
    }

    public void addChildLayouts(Layout... layouts) {
        this.childLayouts.addAll(List.of(layouts));
    }

    public abstract Node render(Container container);
}