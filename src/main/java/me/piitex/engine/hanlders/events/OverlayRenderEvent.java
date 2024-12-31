package me.piitex.engine.hanlders.events;

import me.piitex.engine.overlays.Overlay;

public class OverlayRenderEvent extends Event {
    private final Overlay overlay;

    public OverlayRenderEvent(Overlay overlay) {
        this.overlay = overlay;
    }

    public Overlay getOverlay() {
        return overlay;
    }
}
