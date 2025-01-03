package me.piitex.engine.hanlders.events;

import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import me.piitex.engine.overlays.Overlay;

public class OverlayClickEvent extends Event {
    private final Overlay overlay;
    private final MouseEvent event;


    public OverlayClickEvent(Overlay overlay, MouseEvent event) {
        this.overlay = overlay;
        this.event = event;
    }

    public Overlay getOverlay() {
        return overlay;
    }

    public MouseEvent getHandler() {
        return event;
    }

    public boolean isRightClicked() {
        return event.getButton() == MouseButton.SECONDARY;
    }

    public boolean isMiddleButton() {
        return event.getButton() == MouseButton.MIDDLE;
    }
}
