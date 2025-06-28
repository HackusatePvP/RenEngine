package me.piitex.engine.hanlders.events;

import me.piitex.engine.overlays.Overlay;

public class OverlayCloseEvent extends Event {
    private final Overlay overlay;

    public OverlayCloseEvent(Overlay overlay) {
        this.overlay = overlay;
    }

    public Overlay getOverlay() {
        return overlay;
    }
}
