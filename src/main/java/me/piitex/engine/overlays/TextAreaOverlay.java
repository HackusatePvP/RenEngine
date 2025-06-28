package me.piitex.engine.overlays;

import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import me.piitex.engine.hanlders.events.InputSetEvent;
import me.piitex.engine.loaders.FontLoader;
import me.piitex.engine.overlays.events.IInputSetEvent;
import me.piitex.engine.overlays.events.IOverlaySubmit;
import me.piitex.engine.overlays.events.OverlaySubmitEvent;

public class TextAreaOverlay extends Overlay implements Region {
    private double width, height, prefWidth, prefHeight, maxWidth, maxHeight;
    private double scaleWidth, scaleHeight;
    private final String defaultInput;
    private FontLoader fontLoader;
    private String hintText = "";
    private String currentText;
    private Color textFill = Color.BLACK;
    private boolean renderBorder = true;
    private boolean enabled = true;

    private IInputSetEvent iInputSetEvent;
    private IOverlaySubmit iOverlaySubmit;

    public TextAreaOverlay(String defaultInput, double x, double y, double width, double height) {
        this.defaultInput = defaultInput;
        this.width = width;
        this.height = height;
        setX(x);
        setY(y);
    }

    public TextAreaOverlay(String defaultInput, String hintText, double x, double y, double width, double height) {
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
    }

    public String getCurrentText() {
        return currentText;
    }

    public Color getTextFill() {
        return textFill;
    }

    public void setTextFill(Color textFill) {
        this.textFill = textFill;
    }

    public boolean isRenderBorder() {
        return renderBorder;
    }

    public void setRenderBorder(boolean renderBorder) {
        this.renderBorder = renderBorder;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getHintText() {
        return hintText;
    }

    public void setHintText(String hintText) {
        this.hintText = hintText;
    }

    @Override
    public Node render() {
        TextArea textArea = new TextArea(defaultInput);
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
        textArea.setEditable(enabled);

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
