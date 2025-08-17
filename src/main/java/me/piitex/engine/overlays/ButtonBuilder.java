package me.piitex.engine.overlays;

import javafx.scene.paint.Paint;
import me.piitex.engine.loaders.FontLoader;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.LinkedList;

public class ButtonBuilder {
    private final String id;
    private String text;
    private FontIcon icon;
    private FontLoader font;
    private Paint textFill;
    private double width, height, prefHeight, prefWidth, maxWidth, maxHeight;
    private double scaleWidth, scaleHeight;
    private double x, y;
    private boolean enabled = true;
    private final LinkedList<ImageOverlay> images = new LinkedList<>();

    /**
     * Initializes the builder with the mandatory button ID.
     * @param id The unique identifier for the button.
     */
    public ButtonBuilder(String id) {
        this.id = id;
    }

    /**
     * Sets the text for the button.
     * @param text The text to display on the button.
     * @return The builder instance.
     */
    public ButtonBuilder setText(String text) {
        this.text = text;
        return this;
    }

    /**
     * Sets the icon for the button.
     * @param icon The FontIcon to display on the button.
     * @return The builder instance.
     */
    public ButtonBuilder setIcon(FontIcon icon) {
        this.icon = icon;
        return this;
    }

    /**
     * Sets the font for the button text.
     * @param font The FontLoader for the button.
     * @return The builder instance.
     */
    public ButtonBuilder setFont(FontLoader font) {
        this.font = font;
        return this;
    }

    /**
     * Sets the fill color for the button text.
     * @param textFill The color of the text.
     * @return The builder instance.
     */
    public ButtonBuilder setTextFill(Paint textFill) {
        this.textFill = textFill;
        return this;
    }

    /**
     * Sets the width of the button.
     * @param width The width of the button.
     * @return The builder instance.
     */
    public ButtonBuilder setWidth(double width) {
        this.width = width;
        return this;
    }

    /**
     * Sets the height of the button.
     * @param height The height of the button.
     * @return The builder instance.
     */
    public ButtonBuilder setHeight(double height) {
        this.height = height;
        return this;
    }

    /**
     * Sets the preferred width of the button.
     * @param prefWidth The preferred width.
     * @return The builder instance.
     */
    public ButtonBuilder setPrefWidth(double prefWidth) {
        this.prefWidth = prefWidth;
        return this;
    }

    /**
     * Sets the preferred height of the button.
     * @param prefHeight The preferred height.
     * @return The builder instance.
     */
    public ButtonBuilder setPrefHeight(double prefHeight) {
        this.prefHeight = prefHeight;
        return this;
    }

    /**
     * Sets the maximum width of the button.
     * @param maxWidth The maximum width.
     * @return The builder instance.
     */
    public ButtonBuilder setMaxWidth(double maxWidth) {
        this.maxWidth = maxWidth;
        return this;
    }

    /**
     * Sets the maximum height of the button.
     * @param maxHeight The maximum height.
     * @return The builder instance.
     */
    public ButtonBuilder setMaxHeight(double maxHeight) {
        this.maxHeight = maxHeight;
        return this;
    }

    /**
     * Sets the scale width of the button.
     * @param scaleWidth The scale width.
     * @return The builder instance.
     */
    public ButtonBuilder setScaleWidth(double scaleWidth) {
        this.scaleWidth = scaleWidth;
        return this;
    }

    /**
     * Sets the scale height of the button.
     * @param scaleHeight The scale height.
     * @return The builder instance.
     */
    public ButtonBuilder setScaleHeight(double scaleHeight) {
        this.scaleHeight = scaleHeight;
        return this;
    }

    /**
     * Sets the X and Y position of the button.
     * @param x The X-axis position.
     * @param y The Y-axis position.
     * @return The builder instance.
     */
    public ButtonBuilder setPosition(double x, double y) {
        this.x = x;
        this.y = y;
        return this;
    }

    /**
     * Adds an image to the button.
     * @param image The ImageOverlay to add.
     * @return The builder instance.
     */
    public ButtonBuilder addImage(ImageOverlay image) {
        this.images.add(image);
        return this;
    }

    /**
     * Sets the enabled state of the button.
     * @param enabled True to enable the button, false to disable.
     * @return The builder instance.
     */
    public ButtonBuilder setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    /**
     * Builds and returns a new ButtonOverlay object with the configured properties.
     * @return A new ButtonOverlay instance.
     */
    public ButtonOverlay build() {
        return new ButtonOverlay(this);
    }

    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public FontIcon getIcon() {
        return icon;
    }

    public FontLoader getFont() {
        return font;
    }

    public Paint getTextFill() {
        return textFill;
    }


    public double getWidth() {
        return width;
    }

    public double getPrefWidth() {
        return prefWidth;
    }

    public double getMaxWidth() {
        return maxWidth;
    }

    public double getHeight() {
        return height;
    }

    public double getPrefHeight() {
        return prefHeight;
    }

    public double getMaxHeight() {
        return maxHeight;
    }

    public double getScaleWidth() {
        return scaleWidth;
    }

    public double getScaleHeight() {
        return scaleHeight;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public LinkedList<ImageOverlay> getImages() {
        return images;
    }
}