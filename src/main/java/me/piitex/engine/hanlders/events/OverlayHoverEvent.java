package me.piitex.engine.hanlders.events;

import javafx.scene.input.MouseEvent;
import me.piitex.engine.overlays.Overlay;

public class OverlayHoverEvent extends Event {
    private final Overlay overlay;
    private final MouseEvent mouseEvent;

    public OverlayHoverEvent(Overlay overlay, MouseEvent event) {
        this.overlay = overlay;
        this.mouseEvent = event;
    }

    public Overlay getOverlay() {
        return overlay;
    }

    public MouseEvent getHandler() {
        return mouseEvent;
    }
}
