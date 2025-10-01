package me.piitex.engine.hanlders.events;

import javafx.scene.Node;
import me.piitex.engine.overlays.ColorPickerOverlay;

public class ColorSelectionEvent extends Event {
    private final ColorPickerOverlay colorPickerOverlay;
    private final Node node;

    public ColorSelectionEvent(ColorPickerOverlay colorPickerOverlay, Node node) {
        this.colorPickerOverlay = colorPickerOverlay;
        this.node = node;
    }

    public ColorPickerOverlay getColorPickerOverlay() {
        return colorPickerOverlay;
    }

    public Node getNode() {
        return node;
    }
}
