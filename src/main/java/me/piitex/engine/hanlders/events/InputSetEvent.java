package me.piitex.engine.hanlders.events;

import me.piitex.engine.overlays.Overlay;

public class InputSetEvent extends Event {
    private final Overlay overlay;
    private final String input;

    public InputSetEvent(Overlay overlay, String input) {
        this.overlay = overlay;
        this.input = input;
    }

    public Overlay getOverlay() {
        return overlay;
    }

    public String getInput() {
        return input;
    }
}
