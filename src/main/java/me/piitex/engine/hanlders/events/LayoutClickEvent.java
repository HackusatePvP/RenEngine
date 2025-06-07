package me.piitex.engine.hanlders.events;

import me.piitex.engine.layouts.Layout;

public class LayoutClickEvent extends Event {
    private final Layout layout;

    public LayoutClickEvent(Layout layout) {
        this.layout = layout;
    }

    public Layout getLayout() {
        return layout;
    }
}
