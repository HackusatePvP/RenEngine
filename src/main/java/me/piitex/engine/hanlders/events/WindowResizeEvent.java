package me.piitex.engine.hanlders.events;

import me.piitex.engine.Window;

public class WindowResizeEvent extends Event {
    private final Window window;
    private final Number oldValue, newValue;

    public WindowResizeEvent(Window window, Number oldValue, Number newValue) {
        this.window = window;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public Window getWindow() {
        return window;
    }

    public Number getOldValue() {
        return oldValue;
    }

    public Number getNewValue() {
        return newValue;
    }
}
