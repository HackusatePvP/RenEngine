package me.piitex.engine.hanlders.events;

import javafx.scene.control.Alert;
import me.piitex.engine.overlays.Overlay;

public class AlertConfirmEvent extends Event {
    private final Alert alert;
    private final Overlay overlay;

    public AlertConfirmEvent(Alert alert, Overlay overlay) {
        this.alert = alert;
        this.overlay = overlay;
    }

    public Alert getAlert() {
        return alert;
    }

    public Overlay getOverlay() {
        return overlay;
    }
}
