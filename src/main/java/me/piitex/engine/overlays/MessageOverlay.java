package me.piitex.engine.overlays; // Adjust package to match your overlays

import atlantafx.base.controls.Message;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.event.Event;

// Assuming ImageOverlay is in the same package and can be used directly
import me.piitex.engine.overlays.ImageOverlay;
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

    // Region properties, now explicitly defined in MessageOverlay
    private double width, height, prefWidth, prefHeight, maxWidth, maxHeight;
    private double scaleWidth = 1.0; // Default to 1.0 (no scaling)
    private double scaleHeight = 1.0; // Default to 1.0 (no scaling)

    public MessageOverlay(String title, String description) {
        this.title = title;
        this.description = description;
        this.atlantafxMessage = new Message(title, description);
        initMessageNode();
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
        super(); // Call Overlay's default constructor
        setX(x); // Set X position inherited from Overlay
        setY(y); // Set Y position inherited from Overlay

        this.width = width; // Initialize MessageOverlay's own width
        this.height = height; // Initialize MessageOverlay's own height

        this.title = title;
        this.description = description;
        this.atlantafxMessage = new Message(title, description);
        initMessageNode();
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

        // Render the ImageOverlay to get a JavaFX Node for the graphic
        Node graphicNode = (graphicOverlay != null) ? graphicOverlay.render() : null;
        this.atlantafxMessage = new Message(title, description, graphicNode);
        initMessageNode();
    }

    private void initMessageNode() {
        atlantafxMessage.setLayoutX(getX()); // getX() is from Overlay
        atlantafxMessage.setLayoutY(getY()); // getY() is from Overlay
        atlantafxMessage.setMinSize(width, height);
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public FontIcon getIcon() {
        return icon;
    }

    public void setIcon(FontIcon icon) {
        this.icon = icon;
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

        setInputControls(atlantafxMessage);

        return atlantafxMessage;
    }
}