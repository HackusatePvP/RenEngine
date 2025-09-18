package me.piitex.engine.exceptions;

import me.piitex.engine.Element;

public class NodeNotDefinedException extends Exception {
    private final Element element;

    public NodeNotDefinedException(Element element) {
        this.element = element;
        super("Node for " + element.toString() + " was not defined!");
    }

    public Element getElement() {
        return element;
    }
}
