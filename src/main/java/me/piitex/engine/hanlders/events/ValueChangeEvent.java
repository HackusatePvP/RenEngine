package me.piitex.engine.hanlders.events;

import me.piitex.engine.overlays.Overlay;

public class ValueChangeEvent extends Event {
    private final Overlay overlay;
    private final double oldValue, newValue;

    public ValueChangeEvent(Overlay overlay, double oldValue, double newValue) {
        this.overlay = overlay;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public Overlay getOverlay() {
        return overlay;
    }

    public double getOldValue() {
        return oldValue;
    }

    public double getNewValue() {
        return newValue;
    }
}
