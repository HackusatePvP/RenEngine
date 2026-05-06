package me.piitex.engine.hanlders.events;

import javafx.scene.input.MouseEvent;
import me.piitex.engine.Element;
import me.piitex.engine.overlays.Overlay;

public class ElementClickReleaseEvent extends Event {
    private final Element element;
    private final MouseEvent event;

    public ElementClickReleaseEvent(Element element, MouseEvent event) {
        this.element = element;
        this.event = event;
    }

    public Element getElement() {
        return element;
    }

    public MouseEvent getHandler() {
        return event;
    }
}
