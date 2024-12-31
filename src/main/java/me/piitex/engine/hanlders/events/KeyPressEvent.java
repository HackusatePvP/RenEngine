package me.piitex.engine.hanlders.events;

import javafx.scene.input.KeyEvent;

public class KeyPressEvent extends Event {
    private final KeyEvent event;

    public KeyPressEvent(KeyEvent event) {
        this.event = event;
    }

    public KeyEvent getEvent() {
        return event;
    }
}
