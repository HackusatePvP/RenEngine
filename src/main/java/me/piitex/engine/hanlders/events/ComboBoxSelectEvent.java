package me.piitex.engine.hanlders.events;

import javafx.scene.Node;
import me.piitex.engine.overlays.Overlay;

public class ComboBoxSelectEvent extends Event {
    private final Node node;
    private final Overlay overlay;
    private final String oldValue;
    private final String newValue;

    public ComboBoxSelectEvent(Node node, Overlay overlay, String oldValue, String newValue) {
        this.node = node;
        this.overlay = overlay;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public Node getNode() {
        return node;
    }

    public Overlay getOverlay() {
        return overlay;
    }

    public String getOldValue() {
        return oldValue;
    }

    public String getNewValue() {
        return newValue;
    }
}
