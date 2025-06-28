package me.piitex.engine.hanlders.events;

import javafx.scene.input.MouseEvent;
import me.piitex.engine.layouts.Layout;

public class LayoutClickEvent extends Event {
    private final MouseEvent fxClick;
    private final Layout layout;

    public LayoutClickEvent(MouseEvent fxClick, Layout layout) {
        this.fxClick = fxClick;
        this.layout = layout;
    }

    public MouseEvent getFxClick() {
        return fxClick;
    }

    public Layout getLayout() {
        return layout;
    }
}
