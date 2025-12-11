package me.piitex.engine.overlays;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;
import me.piitex.engine.loaders.FontLoader;

public class TextOverlay extends Overlay {
    private String string;
    private Color textFillColor;
    private FontLoader fontLoader;
    private FontSmoothingType fontSmoothingType;
    private boolean strikeout, underline;
    private final Text textNode;

    public TextOverlay(String text) {
        this.string = text;
        this.textNode = new Text();
        setNode(textNode);
    }

    public TextOverlay(String text, FontLoader fontLoader) {
        this.string = text;
        this.fontLoader = fontLoader;
        this.textNode = new Text();
        setNode(textNode);
    }

    public TextOverlay(String text, Color textFillColor) {
        this.string = text;
        this.textFillColor = textFillColor;
        this.textNode = new Text();
        setNode(textNode);
    }

    public TextOverlay(String text, Color textFillColor, FontLoader fontLoader) {
        this.string = text;
        this.textFillColor = textFillColor;
        this.fontLoader = fontLoader;
        this.textNode = new Text();
        setNode(textNode);
    }

    public TextOverlay(String text, FontLoader fontLoader, double x, double y) {
        this.string = text;
        this.fontLoader = fontLoader;
        this.textNode = new Text();
        setNode(textNode);
        setX(x);
        setY(y);
    }

    public TextOverlay(String text, Color textFillColor, FontLoader fontLoader, int x, int y) {
        this.string = text;
        this.textFillColor = textFillColor;
        this.fontLoader = fontLoader;
        this.textNode = new Text();
        setNode(textNode);
        setX(x);
        setY(y);
    }

    public FontSmoothingType getFontSmoothingType() {
        return fontSmoothingType;
    }

    public void setFontSmoothingType(FontSmoothingType fontSmoothingType) {
        this.fontSmoothingType = fontSmoothingType;
    }

    @Override
    public Node render() {
        // If text is set, render a Text node.
        textNode.setText(string);
        textNode.setFontSmoothingType(fontSmoothingType);
        if (textFillColor != null) {
            textNode.setFill(textFillColor);
        }

        if (fontLoader != null) {
            textNode.setFont(fontLoader.getFont());
        }
        if (getTextFillColor() != null) {
            textNode.setFill(getTextFillColor());
        }
        textNode.setStrikethrough(strikeout);
        textNode.setUnderline(underline);


        textNode.setTranslateX(getX());
        textNode.setTranslateY(getY());
        textNode.getStyleClass().addAll(getStyles());

        return textNode;
    }

    public String getTextNode() {
        return string;
    }

    public void setText(String text) {
        this.string = text;
        textNode.setText(text);
    }

    public boolean isStrikeout() {
        return strikeout;
    }

    public void setStrikeout(boolean strikeout) {
        this.strikeout = strikeout;
        textNode.setStrikethrough(strikeout);
    }

    public boolean isUnderline() {
        return underline;
    }

    public void setUnderline(boolean underline) {
        this.underline = underline;
        textNode.setUnderline(strikeout);
    }

    public FontLoader getFontLoader() {
        return fontLoader;
    }

    public void setFont(FontLoader fontLoader) {
        this.fontLoader = fontLoader;
        textNode.setFont(fontLoader.getFont());
    }

    public Color getTextFillColor() {
        return textFillColor;
    }

    public void setTextFill(Color textFillColor) {
        this.textFillColor = textFillColor;
        textNode.setFill(textFillColor);
    }

}
