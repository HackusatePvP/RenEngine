package me.piitex.engine.overlays;

import atlantafx.base.controls.PasswordTextField;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import me.piitex.engine.hanlders.events.InputSetEvent;
import me.piitex.engine.loaders.FontLoader;
import me.piitex.engine.overlays.events.IInputSetEvent;

public class PasswordFieldOverlay extends Overlay implements Region {
    private final PasswordTextField textField;
    private double width, height, prefWidth, prefHeight, maxWidth, maxHeight;
    private double scaleWidth, scaleHeight;
    private final String defaultInput;
    private FontLoader fontLoader;
    private String hintText = "";
    private IInputSetEvent iInputSetEvent;

    public PasswordFieldOverlay(String defaultInput, String hintText, double x, double y, double width, double height) {
        this.textField = new PasswordTextField();
        this.defaultInput = defaultInput;
        this.width = width;
        this.height = height;
        setNode(textField);
        setX(x);
        setY(y);
    }

    public PasswordFieldOverlay(String defaultInput, String hintText, double width, double height) {
        this(defaultInput, hintText, 0, 0, width, height);
    }

    public PasswordFieldOverlay(String defaultInput, double width, double height) {
        this(defaultInput, "", width, height);
    }

    public PasswordFieldOverlay(double width, double height) {
        this("", "", 0, 0, width, height);
    }

    public String getCurrentText() {
        return textField.getText();
    }

    public void setCurrentText(String currentText) {
        textField.setText(currentText);
    }

    public String getHintText() {
        return hintText;
    }

    public void setHintText(String hintText) {
        this.hintText = hintText;
        textField.setPromptText(hintText);
    }

    public void setEditable(boolean editable) {
        textField.setEditable(editable);
    }

    public PasswordTextField getTextField() {
        return textField;
    }

    @Override
    protected Node render() {
        textField.setTranslateX(getX());
        textField.setTranslateY(getY());
        if (getWidth() > 0 || getHeight() > 0) {
            textField.setMinSize(width, height);
        }
        if (getPrefWidth() > 0 || getPrefHeight() > 0) {
            textField.setPrefSize(getPrefWidth(), getPrefHeight());
        }
        if (getMaxWidth() > 0 || getMaxHeight() > 0) {
            textField.setMaxSize(getMaxWidth(), getMaxHeight());
        }
        textField.setPromptText(hintText);
        textField.setText(defaultInput);
        textField.setAlignment(Pos.CENTER_LEFT);

        setInputControls(textField);
        return textField;
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
        textField.setMinWidth(w);
    }

    @Override
    public void setHeight(double h) {
        this.height = h;
        textField.setMinHeight(h);
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
        textField.setPrefWidth(w);
    }

    @Override
    public void setPrefHeight(double h) {
        this.prefHeight = h;
        textField.setPrefHeight(h);
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
        textField.setMaxWidth(w);
    }

    @Override
    public void setMaxHeight(double h) {
        this.maxHeight = h;
        textField.setMaxHeight(h);
    }
}
