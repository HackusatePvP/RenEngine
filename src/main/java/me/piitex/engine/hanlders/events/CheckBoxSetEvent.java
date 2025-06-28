package me.piitex.engine.hanlders.events;

import javafx.scene.Node;
import me.piitex.engine.overlays.CheckBoxOverlay;

public class CheckBoxSetEvent extends Event {
    private final CheckBoxOverlay overlay;
    private final Node node;
    private final boolean oldValue, newValue;

    public CheckBoxSetEvent(CheckBoxOverlay overlay, Node node, boolean oldValue, boolean newValue) {
        this.overlay = overlay;
        this.node = node;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public CheckBoxOverlay getOverlay() {
        return overlay;
    }

    public Node getNode() {
        return node;
    }

    public boolean getOldValue() {
        return oldValue;
    }

    public boolean getNewValue() {
        return newValue;
    }
}
