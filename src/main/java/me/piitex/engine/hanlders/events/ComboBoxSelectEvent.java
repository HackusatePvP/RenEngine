package me.piitex.engine.hanlders.events;

import javafx.scene.Node;
import me.piitex.engine.overlays.Overlay;

public class ComboBoxSelectEvent extends Event {
    private final Node node;
    private final Overlay overlay;
    private final String item;

    public ComboBoxSelectEvent(Node node, Overlay overlay, String item) {
        this.node = node;
        this.overlay = overlay;
        this.item = item;
    }

    public Node getNode() {
        return node;
    }

    public Overlay getOverlay() {
        return overlay;
    }

    public String getItem() {
        return item;
    }
}
