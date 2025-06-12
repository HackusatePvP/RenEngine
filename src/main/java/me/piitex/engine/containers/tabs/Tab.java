package me.piitex.engine.containers.tabs;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import me.piitex.engine.Container;
import me.piitex.engine.Element;
import me.piitex.engine.layouts.Layout;
import me.piitex.engine.overlays.Overlay;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

public class Tab {
    private final LinkedHashMap<Integer, Element> elements = new LinkedHashMap<>();
    private final String text;
    private double width, height;
    private double x = 20, y = 20;
    private final Container container;

    public Tab(TabsContainer container, String text) {
        this.container = container;
        this.text = text;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }


    /**
     * Adds the element to the specific index. If there is an element already bound to that index it is shuffled forward.
     *
     * @param element The {@link Element} to add to the container.
     * @param index The index/order of the element.
     */
    public void addElement(Element element, int index) {
        Element current = elements.get(index);
        if (current != null) {
            int i = index + 1;
            addElement(getElementAt(index), i);
        }
        elements.put(index, element);
    }

    /**
     * Adds an array of elements to the container. The elements are positioned by the order of the array.
     * The added elements will be indexed to the front of the container.
     * @param elements The array of {@link Element}s to be added.
     */
    public void addElements(Element... elements) {
        for (Element element : elements) {
            addElement(element);
        }
    }

    /**
     * Adds a {@link LinkedList <Element>} of elements to the container. The elements are position by the order of the list.
     * @param elements The list of elements to be added.
     */
    public void addElements(LinkedList<Element> elements) {
        for (Element element : elements) {
            addElement(element);
        }
    }

    public void removeElement(int index) {
        elements.remove(index);
    }

    public void removeAllElement(Element element) {
        LinkedHashMap<Integer, Element> toRemove = new LinkedHashMap<>(elements);
        toRemove.forEach((integer, e) -> {
            if (e == element) {
                elements.remove(integer);
            }
        });
    }

    public void moveElement(int oldIndex, int newIndex) {
        Element element = elements.get(oldIndex);
        if (element != null) {
            elements.put(newIndex, element);
            elements.remove(oldIndex);
        }
    }

    /**
     * Retrieves the current element at the specific index. If the element is not present this will return null.
     * @param index Position of the desired element.
     * @return The {@link Element}
     */
    public Element getElementAt(int index) {
        return elements.get(index);
    }

    /**
     * Adds an element to the container. The added element will be indexed to the front of the container.
     * @param element The {@link Element} to be added.
     */
    public void addElement(Element element) {
        int index = element.getIndex();
        if (index == 0) {
            index = elements.size();
        }

        addElement(element, index);
    }

    public javafx.scene.control.Tab render() {
        Pane content = new Pane();
        content.setTranslateX(getX());
        content.setTranslateY(getY());
        content.setPrefSize(getWidth(), getHeight());
        javafx.scene.control.Tab jTab = new javafx.scene.control.Tab(text);
        jTab.setContent(content);

        LinkedList<Node> toReturn = new LinkedList<>();
        for (Element element : elements.values()) {
            Node node = null;
            if (element instanceof Overlay overlay) {
                node = overlay.render();
            }
            if (element instanceof Layout layout) {
                node = layout.render(container);
            }
            if (node != null) {
                toReturn.add(node);
            }

            // Render sub-containers last
            if (element instanceof Container c) {
                Map.Entry<Node, LinkedList<Node>> entry = c.build();
                toReturn.add(entry.getKey());
            }
        }

        content.getChildren().addAll(toReturn);

        return jTab;
    }
}
