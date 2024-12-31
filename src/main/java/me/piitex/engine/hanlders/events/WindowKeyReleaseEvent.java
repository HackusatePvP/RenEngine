package me.piitex.engine.hanlders.events;

import javafx.scene.input.KeyEvent;
import me.piitex.engine.Window;

public class WindowKeyReleaseEvent {
    private final Window window;
    private final KeyEvent event;

    public WindowKeyReleaseEvent(Window window, KeyEvent event) {
        this.window = window;
        this.event = event;
    }

    public Window getWindow() {
        return window;
    }

    public KeyEvent getEvent() {
        return event;
    }
}
