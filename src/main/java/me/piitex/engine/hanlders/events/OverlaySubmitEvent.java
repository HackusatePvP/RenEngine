package me.piitex.engine.hanlders.events;

import javafx.scene.input.KeyEvent;
import me.piitex.engine.overlays.Overlay;

public class OverlaySubmitEvent extends Event {
    private final Overlay overlay;
    private final KeyEvent keyEvent;

    public OverlaySubmitEvent(Overlay overlay, KeyEvent keyEvent) {
        this.overlay = overlay;
        this.keyEvent = keyEvent;
    }

    public Overlay getOverlay() {
        return overlay;
    }

    public KeyEvent getKeyEvent() {
        return keyEvent;
    }
}
