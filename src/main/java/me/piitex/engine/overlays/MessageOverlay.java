package me.piitex.engine.overlays;

import atlantafx.base.controls.Message;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.event.Event;

import org.kordamp.ikonli.javafx.FontIcon;

/**
 * An overlay that wraps the AtlantaFX Message component, acting as a popup message
 * for warnings or notifications.
 */
public class MessageOverlay extends Overlay implements Region { // Now implements Region

    private final Message atlantafxMessage;
    private String title;
    private String description;
    private FontIcon icon;
    private Overlay graphicOverlay;
    private double width, height, prefWidth, prefHeight, maxWidth, maxHeight;
    private double scaleWidth, scaleHeight;


    public MessageOverlay(String title, String description) {
        this.title = title;
        this.description = description;
        this.atlantafxMessage = new Message(title, description);
        setNode(atlantafxMessage);
    }


    /**
     * Creates a new MessageOverlay.
     *
     * @param x The X-coordinate for the overlay's position.
     * @param y The Y-coordinate for the overlay's position.
     * @param width The width of the message overlay.
     * @param height The height of the message overlay.
     * @param title The title of the message.
     * @param description The description or body text of the message.
     */
    public MessageOverlay(double x, double y, double width, double height, String title, String description) {
        super();
        setX(x);
        setY(y);

        this.width = width;
        this.height = height;

        this.title = title;
        this.description = description;
        this.atlantafxMessage = new Message(title, description);
        setNode(atlantafxMessage);
    }

    /**
     * Creates a new MessageOverlay with a graphic.
     *
     * @param x The X-coordinate for the overlay's position.
     * @param y The Y-coordinate for the overlay's position.
     * @param width The width of the message overlay.
     * @param height The height of the message overlay.
     * @param title The title of the message.
     * @param description The description or body text of the message.
     * @param graphicOverlay An ImageOverlay for the message's icon/graphic.
     */
    public MessageOverlay(double x, double y, double width, double height, String title, String description, Overlay graphicOverlay) {
        setX(x);
        setY(y);

        this.width = width;
        this.height = height;

        this.title = title;
        this.description = description;
        this.graphicOverlay = graphicOverlay;

        Node graphicNode = (graphicOverlay != null) ? graphicOverlay.render() : null;
        this.atlantafxMessage = new Message(title, description, graphicNode);
        setNode(atlantafxMessage);
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        atlantafxMessage.setTitle(title);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        atlantafxMessage.setDescription(description);
    }

    public FontIcon getIcon() {
        return icon;
    }

    public void setIcon(FontIcon icon) {
        this.icon = icon;
        atlantafxMessage.setGraphic(icon);
    }


    public MessageOverlay onAction(Runnable action) {
        atlantafxMessage.setActionHandler(action);
        return this;
    }

    public MessageOverlay onClose(EventHandler<? super Event> handler) {
        atlantafxMessage.setOnClose(handler);
        return this;
    }

    @Override
    public Node render() {
        if (getWidth() > 0 || getHeight() > 0) {
            atlantafxMessage.setMinSize(width, height);
        }
        if (getPrefWidth() > 0 || getPrefHeight() > 0) {
            atlantafxMessage.setPrefSize(getPrefWidth(), getPrefHeight());
        }
        if (getMaxWidth() > 0 || getMaxHeight() > 0) {
            atlantafxMessage.setMaxSize(getMaxWidth(), getMaxHeight());
        }

        atlantafxMessage.setTranslateX(getX());
        atlantafxMessage.setLayoutY(getY());

        atlantafxMessage.setTitle(this.title);
        atlantafxMessage.setDescription(this.description);

        if (icon != null) {
            atlantafxMessage.setGraphic(icon);
        }

        if (graphicOverlay != null) {
            atlantafxMessage.setGraphic(graphicOverlay.render());
        } else {
            atlantafxMessage.setGraphic(null);
        }

        atlantafxMessage.getStyleClass().addAll(getStyles());
        return atlantafxMessage;
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
        atlantafxMessage.setMinWidth(w);
    }

    @Override
    public void setHeight(double h) {
        this.height = h;
        atlantafxMessage.setMinHeight(h);
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
        atlantafxMessage.setPrefWidth(w);
    }

    @Override
    public void setPrefHeight(double h) {
        this.prefHeight = h;
        atlantafxMessage.setPrefHeight(h);
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
        atlantafxMessage.setMaxWidth(w);
    }

    @Override
    public void setMaxHeight(double h) {
        this.maxHeight = h;
        atlantafxMessage.setMaxHeight(h);
    }
}