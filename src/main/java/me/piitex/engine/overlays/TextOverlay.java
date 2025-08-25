package me.piitex.engine.overlays;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import me.piitex.engine.loaders.FontLoader;
import org.kordamp.ikonli.javafx.FontIcon;

public class TextOverlay extends Overlay {
    private final Node node;
    private String string;
    private Color textFillColor;
    private FontLoader fontLoader;
    private boolean strikeout, underline;


    public TextOverlay(String text) {
        this.string = text;
        this.node = new Text();
        setNode(node);
    }

    public TextOverlay(FontIcon icon) {
        this.node = icon;
        setNode(node);
    }

    public TextOverlay(String text, FontLoader fontLoader) {
        this.string = text;
        this.fontLoader = fontLoader;
        this.node = new Text();
        setNode(node);
    }

    public TextOverlay(String text, Color textFillColor) {
        this.string = text;
        this.textFillColor = textFillColor;
        this.node = new Text();
        setNode(node);
    }

    public TextOverlay(String text, Color textFillColor, FontLoader fontLoader) {
        this.string = text;
        this.textFillColor = textFillColor;
        this.fontLoader = fontLoader;
        this.node = new Text();
        setNode(node);
    }

    public TextOverlay(String text, FontLoader fontLoader, double x, double y) {
        this.string = text;
        this.fontLoader = fontLoader;
        this.node = new Text();
        setNode(node);
        setX(x);
        setY(y);
    }

    public TextOverlay(String text, Color textFillColor, FontLoader fontLoader, int x, int y) {
        this.string = text;
        this.textFillColor = textFillColor;
        this.fontLoader = fontLoader;
        this.node = new Text();
        setNode(node);
        setX(x);
        setY(y);
    }

    @Override
    public Node render() {
        if (node instanceof FontIcon icon) {
            if (textFillColor != null) {
                icon.setIconColor(textFillColor);
            }
        } else if (node instanceof Text text) {
            // If text is set, render a Text node.
            text.setText(string);
            if (textFillColor != null) {
                text.setFill(textFillColor);
            }

            if (fontLoader != null) {
                text.setFont(fontLoader.getFont());
            }
            if (getTextFillColor() != null) {
                text.setFill(getTextFillColor());
            }
            text.setStrikethrough(strikeout);
            text.setUnderline(underline);
        }

        if (node != null) {
            node.setTranslateX(getX());
            node.setTranslateY(getY());
            node.getStyleClass().addAll(getStyles());
            setInputControls(node);
        }

        return node;
    }

    public String getText() {
        return string;
    }

    public void setText(String text) {
        this.string = text;

        if (node instanceof Text t) {
            t.setText(text);
        }
    }

    public boolean isStrikeout() {
        return strikeout;
    }

    public void setStrikeout(boolean strikeout) {
        this.strikeout = strikeout;

        if (node instanceof Text t) {
            t.setStrikethrough(strikeout);
        }
    }

    public boolean isUnderline() {
        return underline;
    }

    public void setUnderline(boolean underline) {
        this.underline = underline;

        if (node instanceof Text t) {
            t.setUnderline(strikeout);
        }
    }

    public FontLoader getFontLoader() {
        return fontLoader;
    }

    public void setFont(FontLoader fontLoader) {
        this.fontLoader = fontLoader;

        if (node instanceof Text t) {
            t.setFont(fontLoader.getFont());
        }
    }

    public Color getTextFillColor() {
        return textFillColor;
    }

    public void setTextFill(Color textFillColor) {
        this.textFillColor = textFillColor;

        if (node instanceof Text t) {
            t.setFill(textFillColor);
        }
    }

}
