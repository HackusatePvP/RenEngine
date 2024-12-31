package me.piitex.engine.hanlders.events;

import javafx.scene.input.MouseEvent;
import me.piitex.engine.overlays.Overlay;

public class OverlayDragEvent extends Event {
    private final Overlay overlay;
    private final MouseEvent mouseEvent;

    public OverlayDragEvent(Overlay overlay, MouseEvent mouseEvent) {
        this.overlay = overlay;
        this.mouseEvent = mouseEvent;
    }

    public Overlay getOverlay() {
        return overlay;
    }

    public MouseEvent getMouseEvent() {
        return mouseEvent;
    }
}
