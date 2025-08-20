package me.piitex.engine.overlays;

import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import me.piitex.engine.hanlders.events.InputSetEvent;
import me.piitex.engine.loaders.FontLoader;
import me.piitex.engine.overlays.events.IInputSetEvent;
import me.piitex.engine.overlays.events.IOverlaySubmit;
import me.piitex.engine.hanlders.events.OverlaySubmitEvent;

public class TextAreaOverlay extends Overlay implements Region {
    private final TextArea textArea;
    private double width, height, prefWidth, prefHeight, maxWidth, maxHeight;
    private double scaleWidth, scaleHeight;
    private final String defaultInput;
    private FontLoader fontLoader;
    private String hintText = "";
    private String currentText;

    private IInputSetEvent iInputSetEvent;
    private IOverlaySubmit iOverlaySubmit;

    public TextAreaOverlay(String defaultInput, double x, double y, double width, double height) {
        this.textArea = new TextArea(defaultInput);
        this.defaultInput = defaultInput;
        this.width = width;
        this.height = height;
        setX(x);
        setY(y);
    }

    public TextAreaOverlay(String defaultInput, String hintText, double x, double y, double width, double height) {
        this.textArea = new TextArea(defaultInput);
        this.defaultInput = defaultInput;
        this.hintText = hintText;
        this.width = width;
        this.height = height;
        setX(x);
        setY(y);
    }

    public FontLoader getFontLoader() {
        return fontLoader;
    }

    public void setFont(FontLoader fontLoader) {
        this.fontLoader = fontLoader;
        textArea.setFont(fontLoader.getFont());
    }

    public String getCurrentText() {
        return currentText;
    }

    public void setCurrentText(String currentText) {
        this.currentText = currentText;
        textArea.setText(currentText);
    }

    public String getHintText() {
        return hintText;
    }

    public void setHintText(String hintText) {
        this.hintText = hintText;
        textArea.setPromptText(hintText);
    }

    public void setEditable(boolean editable) {
        textArea.setEditable(editable);
    }

    @Override
    public Node render() {
        textArea.setTranslateX(getX());
        textArea.setTranslateY(getY());
        if (fontLoader != null) {
            textArea.setFont(getFontLoader().getFont());
        }
        textArea.setPrefSize(width, height);
        textArea.setMaxSize(width, height);
        textArea.setPromptText(hintText);
        textArea.setWrapText(true);
        textArea.textProperty().addListener((observable, oldValue, newValue) -> {
            currentText = newValue;
            if (getiInputSetEvent() != null) {
                iInputSetEvent.onInputSet(new InputSetEvent(this, newValue));
            }
        });

        if (getiOverlaySubmit() != null) {
            textArea.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    if (event.isShiftDown()) {
                        textArea.appendText("\n");
                    } else {
                        getiOverlaySubmit().onSubmit(new OverlaySubmitEvent(this, event));
                    }
                }
            });
        }


        textArea.setText(defaultInput);
        setInputControls(textArea);
        return textArea;
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
        textArea.setMinWidth(w);
    }

    @Override
    public void setHeight(double h) {
        this.height = h;
        textArea.setMinHeight(h);
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
        textArea.setPrefWidth(w);
    }

    @Override
    public void setPrefHeight(double h) {
        this.prefHeight = h;
        textArea.setPrefHeight(h);
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
        textArea.setMaxWidth(w);
    }

    @Override
    public void setMaxHeight(double h) {
        this.maxHeight = h;
        textArea.setMaxHeight(h);
    }

    public IInputSetEvent getiInputSetEvent() {
        return iInputSetEvent;
    }

    public void onInputSetEvent(IInputSetEvent iInputSetEvent) {
        this.iInputSetEvent = iInputSetEvent;
    }

    public IOverlaySubmit getiOverlaySubmit() {
        return iOverlaySubmit;
    }

    public void onSubmit(IOverlaySubmit iOverlaySubmit) {
        this.iOverlaySubmit = iOverlaySubmit;
    }
}
