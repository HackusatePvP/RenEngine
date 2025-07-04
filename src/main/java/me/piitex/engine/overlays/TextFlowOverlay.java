package me.piitex.engine.overlays;

import atlantafx.base.util.BBCodeParser;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import me.piitex.engine.loaders.FontLoader;

import java.util.LinkedList;

public class TextFlowOverlay extends Overlay implements Region {
    private LinkedList<Overlay> texts = new LinkedList<>();
    private String text;
    private Color textFillColor;
    private FontLoader font;
    private double width, height, prefWidth, prefHeight, maxWidth, maxHeight;
    private double scaleWidth, scaleHeight;

    public TextFlowOverlay(String text, int width, int height) {
        this.width = width;
        this.height = height;
        this.text = text;
    }

    public TextFlowOverlay(String text, FontLoader fontLoader, int width, int height) {
        this.texts.add(new TextOverlay(text));
        this.font = fontLoader;
        this.width = width;
        this.height = height;
    }

    public TextFlowOverlay(TextOverlay text, int width, int height) {
        this.width = width;
        this.height = height;
        texts.add(text);
    }

    public TextFlowOverlay(LinkedList<Overlay> texts, int width, int height) {
        this.width = width;
        this.height = height;
        this.texts = texts;
    }

    public LinkedList<Overlay> getTexts() {
        return texts;
    }

    public void setTexts(LinkedList<Overlay> texts) {
        this.texts = texts;
    }

    public Color getTextFillColor() {
        return textFillColor;
    }

    public FontLoader getFontLoader() {
        return font;
    }

    public void setFont(FontLoader font) {
        this.font = font;
    }

    public void setTextFillColor(Color textFillColor) {
        this.textFillColor = textFillColor;
    }

    public void add(Overlay overlay) {
        texts.add(overlay);
    }

    @Override
    public Node render() {
        return build();
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


    public TextFlow build() {
        // Creates text that overflows over the box.
        //TextFlow textFlow = BBCodeParser.createFormattedText(text);
        TextFlow textFlow = new TextFlow();
        if (text != null) {
            // Using a VBox fixes all the spacing issues and weird new line stuff.
            VBox format = BBCodeParser.createLayout(text);
            format.setMaxWidth(getMaxWidth());
            format.setMinWidth(getWidth());
            textFlow.getChildren().add(format);
        }

        for (Overlay overlay : texts) {
            // Check node type
            switch (overlay) {
                case TextOverlay text -> {
                    text.setText(text.getText().replace("\\n", System.lineSeparator()));
                    if (font != null && text.getFontLoader() == null) {
                        // Passes
                        text.setFont(font);
                    }
                    if (textFillColor != null) {
                        text.setTextFill(textFillColor);
                    }
                }
                case HyperLinkOverlay hyperlink -> {
                    if (font != null) {
                        hyperlink.setFont(font);
                    }
                }
                case ButtonOverlay button -> {
                    if (font != null) {
                        button.setFont(font);
                    }
                    if (textFillColor != null) {
                        button.setTextFill(textFillColor);
                    }
                }
                case InputFieldOverlay inputField -> {
                    if (font != null) {
                        inputField.setFont(font);
                    }
                }
                default -> System.out.println("Unsupported overlay in TextFlow. {}" + overlay.toString());
            }

            Node node = overlay.render();
            textFlow.getChildren().add(node);
        }

        textFlow.getStyleClass().addAll(getStyles());

        if (getWidth() > 0) {
            textFlow.setMinWidth(getWidth());
        }
        if (getPrefWidth() > 0) {
            textFlow.setPrefWidth(getPrefWidth());
        }
        if (getMaxWidth() > 0) {
            textFlow.setMaxWidth(getMaxWidth());
        }

        if (getHeight() > 0) {
            textFlow.setMinHeight(getHeight());
        }

        if (getPrefHeight() > 0) {
            textFlow.setPrefHeight(getPrefHeight());
        }

        if (getMaxHeight() > 0) {
            textFlow.setMaxHeight(getMaxHeight());
        }

        //textFlow.setTranslateX(getX());
        //textFlow.setTranslateY(getY());
        setInputControls(textFlow);

        textFlow.widthProperty().addListener((obs, oldVal, newVal) -> {
            for (Node node : textFlow.getChildren()) {
                if (node instanceof TextFlow other) {
                    if (getWidth() > 0) {
                        other.setMinWidth(getWidth());
                    }
                    if (getPrefWidth() > 0) {
                        other.setPrefWidth(getPrefWidth());
                    }
                    if (getMaxWidth() > 0) {
                        other.setMaxWidth(getMaxWidth());
                    }

                    if (getHeight() > 0) {
                        other.setMinHeight(getHeight());
                    }

                    if (getPrefHeight() > 0) {
                        other.setPrefHeight(getPrefHeight());
                    }

                    if (getMaxHeight() > 0) {
                        other.setMaxHeight(getMaxHeight());
                    }
                }
            }
        });

        return textFlow;
    }
}
