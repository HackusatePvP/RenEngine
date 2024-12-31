package me.piitex.engine.hanlders.events;

import javafx.scene.input.MouseEvent;
import me.piitex.engine.overlays.Overlay;

public class OverlayClickReleaseEvent extends Event {
    private final Overlay overlay;
    private final MouseEvent event;

    public OverlayClickReleaseEvent(Overlay overlay, MouseEvent event) {
        this.overlay = overlay;
        this.event = event;
    }

    public Overlay getOverlay() {
        return overlay;
    }

    public MouseEvent getHandler() {
        return event;
    }
}
