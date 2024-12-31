package me.piitex.engine.hanlders.events;

import javafx.scene.Node;
import me.piitex.engine.Container;

public class ContainerRenderEvent extends Event {
    private final Container container;
    private final Node node;

    public ContainerRenderEvent(Container container, Node node) {
        this.container = container;
        this.node = node;
    }

    public Container getContainer() {
        return container;
    }

    public Node getNode() {
        return node;
    }
}
