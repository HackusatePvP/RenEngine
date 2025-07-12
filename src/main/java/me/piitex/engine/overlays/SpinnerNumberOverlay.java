package me.piitex.engine.overlays;

import atlantafx.base.util.DoubleStringConverter;
import javafx.scene.Node;
import javafx.scene.control.Spinner;
import me.piitex.engine.hanlders.events.ValueChangeEvent;
import me.piitex.engine.overlays.events.IValueChange;

public class SpinnerNumberOverlay extends Overlay implements Region {
    private final double min, max, def;
    private double width, height, prefWidth, prefHeight, maxWidth, maxHeight;
    private double scaleWidth = 1, scaleHeight = 1;
    private IValueChange change;

    public SpinnerNumberOverlay(double min, double max, double def) {
        this.min = min;
        this.max = max;
        this.def = def;
    }

    public IValueChange getOnValueChange() {
        return change;
    }

    public void onValueChange(IValueChange change) {
        this.change = change;
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

    @Override
    public Node render() {
        Spinner<Double> spinner = new Spinner<>(min, max, def);
        spinner.setTranslateX(getX());
        spinner.setTranslateY(getY());
        spinner.getStyleClass().addAll(getStyles());
        DoubleStringConverter.createFor(spinner);
        spinner.setEditable(true);

        if (getWidth() > 0) {
            spinner.setMinWidth(getWidth());
        }
        if (getPrefWidth() > 0) {
            spinner.setPrefWidth(getPrefWidth());
        }
        if (getMaxWidth() > 0) {
            spinner.setMaxWidth(getMaxWidth());
        }

        if (getHeight() > 0) {
            spinner.setMinHeight(getHeight());
        }

        if (getPrefHeight() > 0) {
            spinner.setPrefHeight(getPrefHeight());
        }

        if (getMaxHeight() > 0) {
            spinner.setMaxHeight(getMaxHeight());
        }


        if (getOnValueChange() != null) {
            spinner.valueProperty().addListener((observable, oldValue, newValue) -> {
                getOnValueChange().onValueChange(new ValueChangeEvent(this, oldValue, newValue));
            });
        }

        return spinner;
    }
}
