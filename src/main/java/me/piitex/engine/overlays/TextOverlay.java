package me.piitex.engine.overlays;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import me.piitex.engine.loaders.FontLoader;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.javafx.StackedFontIcon;

import java.io.File;
import java.net.MalformedURLException;

public class TextOverlay extends Overlay implements Region {
    private String string;
    private FontIcon icon;
    private Color textFillColor;
    private FontLoader fontLoader;
    private double width, height, prefWidth, prefHeight, maxWidth, maxHeight;
    private double scaleWidth, scaleHeight;
    private boolean strikeout, underline;


    public TextOverlay(String text) {
        this.string = text;
    }

    public TextOverlay(FontIcon icon) {
        this.icon = icon;
    }

    public TextOverlay(String text, FontLoader fontLoader) {
        this.string = text;
        this.fontLoader = fontLoader;
    }

    public TextOverlay(String text, Color textFillColor) {
        this.string = text;
        this.textFillColor = textFillColor;
    }

    public TextOverlay(String text, Color textFillColor, FontLoader fontLoader) {
        this.string = text;
        this.textFillColor = textFillColor;
        this.fontLoader = fontLoader;
    }

    public TextOverlay(String text, FontLoader fontLoader, double x, double y) {
        this.string = text;
        this.fontLoader = fontLoader;
        setX(x);
        setY(y);
    }

    public TextOverlay(String text, Color textFillColor, FontLoader fontLoader, int x, int y) {
        this.string = text;
        this.textFillColor = textFillColor;
        this.fontLoader = fontLoader;
        setX(x);
        setY(y);
    }

    @Override
    public Node render() {

        Node nodeToRender = null;

        if (icon != null) {
            icon.setTranslateX(getX());
            icon.setTranslateY(getY());
            if (textFillColor != null) {
                icon.setIconColor(textFillColor);
            }

            nodeToRender = icon;
        } else if (string != null) {
            // If text is set, render a Text node.
            Text textNode = new Text(string);
            if (textFillColor != null) {
                textNode.setFill(textFillColor);
            }
            // Apply overlay's position (x, y) to the Text node
            textNode.setTranslateX(getX());
            textNode.setTranslateX(getY());

            textNode.setWrappingWidth(width);

            // Apply styling from the base Overlay class if you want text to inherit
            if (fontLoader != null) {
                textNode.setFont(fontLoader.getFont());
            }
            if (getTextFillColor() != null) {
                textNode.setFill(getTextFillColor());
            }
            textNode.setStrikethrough(strikeout);
            textNode.setUnderline(underline);

            nodeToRender = textNode;
        }

        // Apply common overlay properties like scale and styleFx
        if (nodeToRender != null) {
            nodeToRender.getStyleClass().addAll(getStyles());
            // Your existing input controls setup
            setInputControls(nodeToRender);
        }

        return nodeToRender;
    }

    public String getText() {
        return string;
    }

    public void setText(String text) {
        this.string = text;
    }

    public boolean isStrikeout() {
        return strikeout;
    }

    public void setStrikeout(boolean strikeout) {
        this.strikeout = strikeout;
    }

    public boolean isUnderline() {
        return underline;
    }

    public void setUnderline(boolean underline) {
        this.underline = underline;
    }

    public FontLoader getFontLoader() {
        return fontLoader;
    }

    public void setFont(FontLoader fontLoader) {
        this.fontLoader = fontLoader;
    }

    public Color getTextFillColor() {
        return textFillColor;
    }

    public void setTextFill(Color textFillColor) {
        this.textFillColor = textFillColor;
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
