package me.piitex.engine.hanlders.events;

import me.piitex.engine.overlays.ComboBoxOverlay;

public class ComboBoxSelectEvent extends Event {
    private final ComboBoxOverlay overlay;
    private final String item;

    public ComboBoxSelectEvent(ComboBoxOverlay overlay, String item) {
        this.overlay = overlay;
        this.item = item;
    }

    public ComboBoxOverlay getOverlay() {
        return overlay;
    }

    public String getItem() {
        return item;
    }
}
