package me.piitex.engine.overlays;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import me.piitex.engine.loaders.FontLoader;
import org.kordamp.ikonli.javafx.FontIcon;

public class TextOverlay extends Overlay implements Region {
    private String string;
    private FontIcon icon;
    private Color textFillColor;
    private FontLoader fontLoader;
    private double width, height;
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
        Text text;
        if (string != null) {
            text = new Text(getText());
            if (fontLoader != null) {
                Font font = fontLoader.getFont();
                text.setFont(font);
            }
            text.setStrikethrough(strikeout);
            if (getTextFillColor() != null) {
                text.setFill(getTextFillColor());
            }
            text.setUnderline(underline);
            return text;
        } else {
            text = icon;
        }

        text.setTranslateX(getX());
        text.setTranslateY(getY());
        setInputControls(text);
        return text;
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
