package me.piitex.engine.hanlders.events;

import javafx.scene.input.KeyEvent;

public class KeyReleaseEvent extends Event {
    private final KeyEvent event;

    public KeyReleaseEvent(KeyEvent event) {
        this.event = event;
    }


    public KeyEvent getEvent() {
        return event;
    }
}
