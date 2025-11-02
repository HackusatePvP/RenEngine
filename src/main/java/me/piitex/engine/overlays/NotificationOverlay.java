package me.piitex.engine.overlays;

import atlantafx.base.controls.Notification;
import javafx.scene.Node;
import me.piitex.engine.hanlders.events.OverlayCloseEvent;
import me.piitex.engine.overlays.events.IOverlayClose;

public class NotificationOverlay extends Overlay implements Region {
    private final Notification notification;
    private String message;
    private Node graphic;
    private double width, height, prefWidth, prefHeight, maxWidth, maxHeight;
    private double scaleWidth, scaleHeight;

    private IOverlayClose overlayClose;

    public NotificationOverlay(String message) {
        this(message, null);
    }

    public NotificationOverlay(String message, Node graphic) {
        this.message = message;
        this.graphic = graphic;
        this.notification = new Notification(message, graphic);
        setNode(notification);
    }

    @Override
    public Node render() {
        notification.setTranslateX(getX());
        notification.setTranslateY(getY());

        if (getWidth() > 0 || getHeight() > 0) {
            notification.setMinSize(width, height);
        }
        if (getPrefWidth() > 0 || getPrefHeight() > 0) {
            notification.setPrefSize(getPrefWidth(), getPrefHeight());
        }
        if (getMaxWidth() > 0 || getMaxHeight() > 0) {
            notification.setMaxSize(getMaxWidth(), getMaxHeight());
        }

        notification.setOnClose(event -> {
            if (overlayClose != null) {
                overlayClose.onClose(new OverlayCloseEvent(this));
            }
        });
        notification.getStyleClass().addAll(getStyles());
        return notification;
    }

    public void setMessage(String message) {
        this.message = message;
        notification.setMessage(message);
    }

    public String getMessage() {
        return message;
    }

    public void setGraphic(Node graphic) {
        this.graphic = graphic;
        notification.setGraphic(graphic);
    }

    public Node getGraphic() {
        return graphic;
    }

    public void onClose(IOverlayClose overlayClose) {
        this.overlayClose = overlayClose;
    }

    @Override
    public double getWidth() {
        return width;
    }

    @Override
    public double getHeight() {
        return height;
    }

    @Override
    public void setWidth(double w) {
        this.width = w;
        notification.setMinWidth(w);
    }

    @Override
    public void setHeight(double h) {
        this.height = h;
        notification.setMinHeight(h);
    }

    @Override
    public double getPrefWidth() {
        return prefWidth;
    }

    @Override
    public double getPrefHeight() {
        return prefHeight;
    }

    @Override
    public void setPrefWidth(double w) {
        this.prefWidth = w;
        notification.setPrefWidth(w);
    }

    @Override
    public void setPrefHeight(double h) {
        this.prefHeight = h;
        notification.setPrefHeight(h);
    }

    @Override
    public double getMaxWidth() {
        return maxWidth;
    }

    @Override
    public double getMaxHeight() {
        return maxHeight;
    }

    @Override
    public void setMaxWidth(double w) {
        this.maxWidth = w;
        notification.setMaxWidth(w);
    }

    @Override
    public void setMaxHeight(double h) {
        this.maxHeight = h;
        notification.setMaxHeight(h);
    }

}