package me.piitex.engine.hanlders.events;

import javafx.scene.layout.Pane;
import me.piitex.engine.layouts.Layout;

public class LayoutRenderEvent extends Event {
    private final Pane node;
    private final Layout layout;

    public LayoutRenderEvent(Pane node, Layout layout) {
        this.node = node;
        this.layout = layout;
    }

    public Layout getLayout() {
        return layout;
    }

    public Pane getNode() {
        return node;
    }
}
