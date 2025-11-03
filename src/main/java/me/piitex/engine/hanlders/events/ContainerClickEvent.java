package me.piitex.engine.hanlders.events;

import me.piitex.engine.containers.Container;

public class ContainerClickEvent extends Event {
    private final Container container;

    public ContainerClickEvent(Container container) {
        this.container = container;
    }

    public Container getContainer() {
        return container;
    }
}
