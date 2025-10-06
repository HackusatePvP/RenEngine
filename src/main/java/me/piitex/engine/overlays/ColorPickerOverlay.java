package me.piitex.engine.overlays;

import javafx.scene.Node;
import javafx.scene.control.ColorPicker;
import javafx.scene.paint.Color;
import me.piitex.engine.hanlders.events.ColorSelectionEvent;
import me.piitex.engine.overlays.events.IColorSelect;

public class ColorPickerOverlay extends Overlay implements Region {
    private final ColorPicker colorPicker;
    private double width, height, prefWidth, prefHeight, maxWidth, maxHeight;
    private IColorSelect colorSelect;

    public ColorPickerOverlay() {
        this.colorPicker = new ColorPicker();
        setNode(colorPicker);
    }

    public IColorSelect getColorSelect() {
        return colorSelect;
    }

    public Color getValue() {
        return colorPicker.getValue();
    }

    public void setValue(Color color) {
        colorPicker.setValue(color);
    }

    public void onColorSelect(IColorSelect colorSelect) {
        this.colorSelect = colorSelect;
        colorPicker.setOnAction(event -> {
            if (colorSelect != null) {
                colorSelect.onColorSelect(new ColorSelectionEvent(this, colorPicker));
            }
        });
    }

    @Override
    protected Node render() {
        colorPicker.setTranslateX(getX());
        colorPicker.setTranslateY(getY());
        colorPicker.setOnAction(event -> {
            if (getColorSelect() != null) {
                getColorSelect().onColorSelect(new ColorSelectionEvent(this, colorPicker));
            }
        });

        return colorPicker;
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
        colorPicker.setMinWidth(w);
    }

    @Override
    public void setHeight(double h) {
        this.height = h;
        colorPicker.setMinHeight(h);
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
        colorPicker.setPrefWidth(w);
    }

    @Override
    public void setPrefHeight(double h) {
        this.prefHeight = h;
        colorPicker.setPrefHeight(h);
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
        colorPicker.setMaxWidth(w);
    }

    @Override
    public void setMaxHeight(double h) {
        this.maxHeight = h;
        colorPicker.setMaxHeight(h);
    }

}
