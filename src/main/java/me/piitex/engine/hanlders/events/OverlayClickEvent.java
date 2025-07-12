package me.piitex.engine.hanlders.events;

import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import me.piitex.engine.overlays.Overlay;

public class OverlayClickEvent extends Event {
    private final Overlay overlay;
    private final MouseEvent event;
    private final double x, y;


    public OverlayClickEvent(Overlay overlay, MouseEvent event, double x, double y) {
        this.overlay = overlay;
        this.event = event;
        this.x = x;
        this.y = y;
    }

    public Overlay getOverlay() {
        return overlay;
    }


    public double getX() {
        return x;
    }

    public double getY() {
        return y;
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
