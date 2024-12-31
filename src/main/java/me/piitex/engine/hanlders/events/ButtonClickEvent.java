package me.piitex.engine.hanlders.events;

import javafx.scene.control.Button;

public class ButtonClickEvent extends Event {
    private final Button button;

    public ButtonClickEvent(Button button) {
        this.button = button;
    }

    public Button getButton() {
        return button;
    }
}