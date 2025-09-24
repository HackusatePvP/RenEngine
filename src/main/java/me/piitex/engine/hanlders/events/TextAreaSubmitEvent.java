package me.piitex.engine.hanlders.events;

import me.piitex.engine.overlays.Overlay;

public class TextAreaSubmitEvent extends Event {
    private final Overlay overlay;
    private final String text;

    public TextAreaSubmitEvent(Overlay overlay, String text) {
        this.overlay = overlay;
        this.text = text;
    }

    public Overlay getOverlay() {
        return overlay;
    }

    public String getText() {
        return text;
    }
}
