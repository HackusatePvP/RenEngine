package me.piitex.engine.overlays;

import javafx.scene.Node;
import javafx.scene.control.Spinner;
import me.piitex.engine.hanlders.events.ValueChangeEvent;
import me.piitex.engine.overlays.events.IValueChange;

public class SpinnerNumberOverlay extends Overlay implements Region {
    private final Spinner<Number> spinner;
    private final Number min, max, def;
    private double width, height, prefWidth, prefHeight, maxWidth, maxHeight;
    private double scaleWidth = 1, scaleHeight = 1;
    private IValueChange change;

    public SpinnerNumberOverlay(double min, double max, double def) {
        this.min = min;
        this.max = max;
        this.def = def;
        this.spinner = new Spinner<>(min, max, def);
    }

    public SpinnerNumberOverlay(int min, int max, int def) {
        this.min = min;
        this.max = max;
        this.def = def;
        this.spinner = new Spinner<>(min, max, def);
    }

    public Number getMin() {
        return min;
    }

    public Number getMax() {
        return max;
    }

    public Number getInitialValue() {
        return def;
    }

    public IValueChange getOnValueChange() {
        return change;
    }

    public void onValueChange(IValueChange change) {
        this.change = change;

        spinner.valueProperty().addListener((observable, oldValue, newValue) -> {
            change.onValueChange(new ValueChangeEvent(this, oldValue, newValue));
        });
    }

    public Number getCurrentValue() {
        return spinner.getValue();
    }

    public void setCurrentValue(Number number) {
        spinner.getValueFactory().setValue(number);
    }

    @Override
    public Node render() {
        spinner.setTranslateX(getX());
        spinner.setTranslateY(getY());
        spinner.getStyleClass().addAll(getStyles());
        //DoubleStringConverter.createFor(spinner);
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

        return spinner;
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
        spinner.setMinWidth(w);
    }

    @Override
    public void setHeight(double h) {
        this.height = h;
        spinner.setMinHeight(h);
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
        spinner.setPrefWidth(w);
    }

    @Override
    public void setPrefHeight(double h) {
        this.prefHeight = h;
        spinner.setPrefHeight(h);
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
        spinner.setMaxWidth(w);
    }

    @Override
    public void setMaxHeight(double h) {
        this.maxHeight = h;
        spinner.setMaxHeight(h);
    }
}
