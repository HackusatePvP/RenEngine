package me.piitex.engine.hanlders.events;

import javafx.scene.input.MouseEvent;
import me.piitex.engine.Element;
import me.piitex.engine.overlays.Overlay;

public class ElementHoverEvent extends Event {
    private final Element element;
    private final MouseEvent mouseEvent;

    public ElementHoverEvent(Element element, MouseEvent event) {
        this.element = element;
        this.mouseEvent = event;
    }

    public Element getElement() {
        return element;
    }

    public MouseEvent getHandler() {
        return mouseEvent;
    }
}
