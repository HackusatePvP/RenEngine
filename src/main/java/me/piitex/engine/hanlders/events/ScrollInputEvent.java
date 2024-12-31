package me.piitex.engine.hanlders.events;

public class ScrollInputEvent extends Event {
    private final javafx.scene.input.ScrollEvent scrollEvent;

    public ScrollInputEvent(javafx.scene.input.ScrollEvent scrollEvent) {
        this.scrollEvent = scrollEvent;
    }

    public javafx.scene.input.ScrollEvent getScrollEvent() {
        return scrollEvent;
    }
}
