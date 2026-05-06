package me.piitex.engine.hanlders.events;

import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import me.piitex.engine.Element;
import me.piitex.engine.overlays.Overlay;

public class ElementClickEvent extends Event {
    private final Element element;
    private final MouseEvent event;
    private final double x, y;


    public ElementClickEvent(Element element, MouseEvent event, double x, double y) {
        this.element = element;
        this.event = event;
        this.x = x;
        this.y = y;
    }

    public Element getElement() {
        return element;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public MouseEvent getHandler() {
        return event;
    }

    public boolean isRightClicked() {
        return event.getButton() == MouseButton.SECONDARY;
    }

    public boolean isMiddleButton() {
        return event.getButton() == MouseButton.MIDDLE;
    }
}
