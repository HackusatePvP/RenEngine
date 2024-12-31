package me.piitex.engine.hanlders.events;

import javafx.scene.input.KeyEvent;
import me.piitex.engine.Window;

public class WindowKeyPressEvent extends Event {
    private final Window window;
    private final KeyEvent keyEvent;

    public WindowKeyPressEvent(Window window, KeyEvent keyEvent) {
        this.window = window;
        this.keyEvent = keyEvent;
    }

    public Window getWindow() {
        return window;
    }

    public KeyEvent getKeyEvent() {
        return keyEvent;
    }
}
