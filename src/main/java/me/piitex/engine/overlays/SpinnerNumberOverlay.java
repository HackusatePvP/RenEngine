package me.piitex.engine.overlays;

import atlantafx.base.util.DoubleStringConverter;
import javafx.scene.Node;
import javafx.scene.control.Spinner;
import me.piitex.engine.hanlders.events.ValueChangeEvent;
import me.piitex.engine.overlays.events.IValueChange;

public class SpinnerNumberOverlay extends Overlay {
    private final double min, max, def;
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
    public Node render() {
        Spinner<Double> spinner = new Spinner<>(min, max, def);
        spinner.setTranslateX(getX());
        spinner.setTranslateY(getY());
        spinner.getStyleClass().addAll(getStyles());
        DoubleStringConverter.createFor(spinner);
        spinner.setEditable(true);

        if (getOnValueChange() != null) {
            spinner.valueProperty().addListener((observable, oldValue, newValue) -> {
                getOnValueChange().onValueChange(new ValueChangeEvent(this, oldValue, newValue));
            });
        }

        return spinner;
    }
}
