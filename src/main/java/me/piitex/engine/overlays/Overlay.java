package me.piitex.engine.overlays;

import javafx.scene.Node;
import me.piitex.engine.DisplayOrder;
import me.piitex.engine.hanlders.events.MouseClickEvent;
import me.piitex.engine.hanlders.events.OverlayClickEvent;
import me.piitex.engine.overlays.events.IOverlayClick;
import me.piitex.engine.overlays.events.IOverlayClickRelease;
import me.piitex.engine.overlays.events.IOverlayHover;
import me.piitex.engine.overlays.events.IOverlayHoverExit;

public abstract class Overlay {
    private double x,y;
    private double scaleX, scaleY;
    private DisplayOrder order = DisplayOrder.NORMAL;

    private IOverlayHover iOverlayHover;
    private IOverlayHoverExit iOverlayHoverExit;
    private IOverlayClick iOverlayClick;
    private IOverlayClickRelease iOverlayClickRelease;

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

    public double getScaleX() {
        return scaleX;
    }

    public void setScaleX(double scaleX) {
        this.scaleX = scaleX;
    }

    public double getScaleY() {
        return scaleY;
    }

    public void setScaleY(double scaleY) {
        this.scaleY = scaleY;
    }

    public DisplayOrder getOrder() {
        return order;
    }

    public void setOrder(DisplayOrder order) {
        this.order = order;
    }

    public void onClick(IOverlayClick iOverlayClick) {
        this.iOverlayClick = iOverlayClick;
    }

    public void onHover(IOverlayHover iOverlayHover) {
        this.iOverlayHover = iOverlayHover;
    }

    public void onClickRelease(IOverlayClickRelease iOverlayClickRelease) {
        this.iOverlayClickRelease = iOverlayClickRelease;
    }

    public void onHoverExit(IOverlayHoverExit iOverlayHoverExit) {
        this.iOverlayHoverExit = iOverlayHoverExit;
    }

    public IOverlayClick getOnClick() {
        return iOverlayClick;
    }

    public IOverlayHover getOnHover() {
        return iOverlayHover;
    }

    public IOverlayHoverExit getOnHoverExit() {
        return iOverlayHoverExit;
    }

    public IOverlayClickRelease getOnRelease() {
        return iOverlayClickRelease;
    }


    public abstract Node render();

    public void setInputControls(Node node) {
        if (node.getOnDragEntered() == null) {
            node.setOnMouseEntered(event -> {

            });
        }
        if (node.getOnMouseClicked() == null) {
            node.setOnMouseClicked(event -> {
                MouseClickEvent clickEvent = new MouseClickEvent(event);
                OverlayClickEvent overlayClickEvent = new OverlayClickEvent(this, event);
                if (getOnClick() != null) {
                    getOnClick().onClick(overlayClickEvent);
                }
            });
        }
        if (node.getOnMouseExited() == null) {
            node.setOnMouseExited(event -> {

            });
        }
        if (node.getOnMouseReleased() == null) {
            node.setOnMouseReleased(event -> {

            });
        }
    }
}
