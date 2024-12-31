package me.piitex.engine.hanlders.events;

import javafx.scene.input.MouseEvent;

public class MouseClickEvent extends Event {
    private final MouseEvent event;

    public MouseClickEvent(MouseEvent event) {
        this.event = event;
    }

    public MouseEvent getEvent() {
        return event;
    }
}
