package me.piitex.engine.hanlders.events;

import me.piitex.engine.overlays.Overlay;

public class ValueChangeEvent extends Event {
    private final Overlay overlay;
    private final Number oldValue, newValue;

    public ValueChangeEvent(Overlay overlay, Number oldValue, Number newValue) {
        this.overlay = overlay;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public Overlay getOverlay() {
        return overlay;
    }

    public Number getOldValue() {
        return oldValue;
    }

    public Number getNewValue() {
        return newValue;
    }
}
