package me.piitex.engine.overlays;

import atlantafx.base.controls.ToggleSwitch;
import javafx.scene.Node;
import me.piitex.engine.hanlders.events.ToggleSwitchEvent;
import me.piitex.engine.overlays.events.IToggleSwitch;

public class ToggleSwitchOverlay extends Overlay {
    private final boolean defaultValue;
    private boolean currentValue = true;

    private IToggleSwitch event;

    public ToggleSwitchOverlay(boolean defaultValue) {
        this.defaultValue = defaultValue;
        currentValue = defaultValue;
    }

    public boolean getDefaultValue() {
        return defaultValue;
    }

    public boolean getCurrentValue() {
        return currentValue;
    }

    public IToggleSwitch getOnToggle() {
        return event;
    }

    public void onToggle(IToggleSwitch event) {
        this.event = event;
    }

    @Override
    public Node render() {
        ToggleSwitch toggleSwitch = new ToggleSwitch();
        toggleSwitch.setTranslateX(getX());
        toggleSwitch.setTranslateY(getY());
        toggleSwitch.getStyleClass().addAll(getStyles());
        toggleSwitch.selectedProperty().set(defaultValue);

        if (event != null) {
            toggleSwitch.selectedProperty().addListener(((observable, oldValue, newValue) -> {
                event.onToggle(new ToggleSwitchEvent(this, oldValue, newValue));
            }));
        }

        return toggleSwitch;
    }
}
