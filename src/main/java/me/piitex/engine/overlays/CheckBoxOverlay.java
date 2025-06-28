package me.piitex.engine.overlays;

import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import me.piitex.engine.hanlders.events.CheckBoxSetEvent;
import me.piitex.engine.overlays.events.ICheckBoxSet;

public class CheckBoxOverlay extends Overlay {
    private boolean defaultValue = false;
    private String label;

    private ICheckBoxSet checkBoxSet;

    public CheckBoxOverlay(boolean check, String label) {
        this.defaultValue = check;
        this.label = label;
    }

    public boolean isDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(boolean defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public ICheckBoxSet getCheckBoxSet() {
        return checkBoxSet;
    }

    public void onSet(ICheckBoxSet checkBoxSet) {
        this.checkBoxSet = checkBoxSet;
    }

    @Override
    public Node render() {
        CheckBox checkBox = new CheckBox(label);
        checkBox.setSelected(defaultValue);

        checkBox.setTranslateX(getX());
        checkBox.setTranslateY(getY());

        checkBox.getStyleClass().addAll(getStyles());

        checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (getCheckBoxSet() != null) {
                getCheckBoxSet().onSet(new CheckBoxSetEvent(this, checkBox, oldValue, newValue));
            }
        });

        return checkBox;
    }
}
