package me.piitex.engine.hanlders.events;

import me.piitex.engine.overlays.ToggleSwitchOverlay;

public class ToggleSwitchEvent extends Event {
    private final ToggleSwitchOverlay overlay;
    private final boolean newValue;
    private final boolean oldValue;

    public ToggleSwitchEvent(ToggleSwitchOverlay overlay, boolean newValue, boolean oldValue) {
        this.overlay = overlay;
        this.newValue = newValue;
        this.oldValue = oldValue;
    }

    public ToggleSwitchOverlay getOverlay() {
        return overlay;
    }

    public boolean getOldValue() {
        return oldValue;
    }

    public boolean getNewValue() {
        return newValue;
    }
}
