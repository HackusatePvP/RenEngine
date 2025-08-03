package me.piitex.engine.hanlders.events;

import me.piitex.engine.overlays.Overlay;

public class ValueChangeEvent extends Event {
    private final Overlay overlay;
    private final Object oldValue, newValue;

    public ValueChangeEvent(Overlay overlay, Object oldValue, Object newValue) {
        this.overlay = overlay;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public Overlay getOverlay() {
        return overlay;
    }

    public Object getOldValue() {
        return oldValue;
    }

    public Object getNewValue() {
        return newValue;
    }
}
