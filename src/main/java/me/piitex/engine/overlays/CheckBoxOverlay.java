package me.piitex.engine.overlays;

import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import me.piitex.engine.hanlders.events.CheckBoxSetEvent;
import me.piitex.engine.overlays.events.ICheckBoxSet;

public class CheckBoxOverlay extends Overlay {
    private final CheckBox checkBox;
    private boolean selected;
    private boolean defaultValue;
    private String label;

    private ICheckBoxSet checkBoxSet;

    public CheckBoxOverlay(boolean check, String label) {
        this.checkBox = new CheckBox(label);
        this.defaultValue = check;
        this.label = label;
    }

    public boolean isDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(boolean defaultValue) {
        this.defaultValue = defaultValue;
        checkBox.setSelected(defaultValue);
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
        checkBox.setText(label);
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        checkBox.setSelected(selected);
    }

    public ICheckBoxSet getCheckBoxSet() {
        return checkBoxSet;
    }

    public void onSet(ICheckBoxSet checkBoxSet) {
        this.checkBoxSet = checkBoxSet;
    }

    @Override
    public Node render() {
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
