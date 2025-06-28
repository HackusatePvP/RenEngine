package me.piitex.engine.overlays;

import atlantafx.base.controls.Notification;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.collections.ObservableList;
import me.piitex.engine.hanlders.events.OverlayCloseEvent;
import me.piitex.engine.overlays.events.IOverlayClose;



public class NotificationOverlay extends Overlay implements Region {
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
    }

    @Override
    public Node render() {
        Notification notification = new Notification(message, graphic);
        notification.setTranslateX(getX());
        notification.setTranslateY(getY());
        notification.setPrefSize(width, height);

        notification.setOnClose(event -> {
            if (overlayClose != null) {
                overlayClose.onClose(new OverlayCloseEvent(this));
            }
        });

        setInputControls(notification);
        notification.getStyleClass().addAll(getStyles());
        return notification;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setGraphic(Node graphic) {
        this.graphic = graphic;
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
    }

    @Override
    public void setHeight(double h) {
        this.height = h;
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
    }

    @Override
    public void setPrefHeight(double h) {
        this.prefHeight = h;
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
    }

    @Override
    public void setMaxHeight(double h) {
        this.maxHeight = h;
    }

    @Override
    public double getScaleWidth() {
        return scaleWidth;
    }

    @Override
    public void setScaleWidth(double w) {
        this.scaleWidth = w;
    }

    @Override
    public double getScaleHeight() {
        return scaleHeight;
    }

    @Override
    public void setScaleHeight(double h) {
        this.scaleHeight = h;
    }

}