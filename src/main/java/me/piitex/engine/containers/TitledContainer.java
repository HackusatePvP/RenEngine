package me.piitex.engine.containers;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.Pane;
import me.piitex.engine.Container;
import me.piitex.engine.Element;
import me.piitex.engine.layouts.VerticalLayout;

import java.util.AbstractMap;
import java.util.LinkedList;
import java.util.Map;

public class TitledContainer extends Container {
    private final String title;
    private boolean collapse = true;
    private final VerticalLayout layout;
    private Pos alignment;
    private double spacing = 10;

    public TitledContainer(String title, double width, double height) {
        super(0, 0, width, height);
        this.title = title;
         layout = new VerticalLayout(getWidth(), getHeight());
    }

    public TitledContainer(String title, double x, double y, double width, double height) {
        super(x, y, width, height);
        this.title = title;
        layout = new VerticalLayout(getWidth(), getHeight());
    }

    public void setCollapse(boolean collapse) {
        this.collapse = collapse;
    }

    public void setAlignment(Pos alignment) {
        this.alignment = alignment;
    }

    public void setSpacing(double spacing) {
        this.spacing = spacing;
    }

    @Override
    public void addElement(Element element, int index) {
        layout.addElement(element, index);
    }

    @Override
    public void addElement(Element element) {
        layout.addElement(element);
    }

    @Override
    public void addElements(Element... elements) {
        layout.addElements(elements);
    }

    @Override
    public void addElements(LinkedList<Element> elements) {
        layout.addElements(elements);
    }

    @Override
    public Map.Entry<Node, LinkedList<Node>> build() {
        TitledPane pane = new TitledPane();
        pane.setText(title);
        pane.setTranslateX(getX());
        pane.setTranslateY(getY());
        if (getWidth() > 0) {
            pane.setMinWidth(getWidth());
        }
        if (getHeight() > 0) {
            pane.setMinHeight(getHeight());
        }

        if (getPrefWidth() > 0) {
            pane.setPrefWidth(getPrefWidth());
        }
        if (getPrefHeight() > 0) {
            pane.setPrefHeight(getPrefHeight());
        }

        if (getMaxWidth() > 0) {
            pane.setMaxWidth(getMaxWidth());
        }
        if (getMaxHeight() > 0) {
            pane.setMaxHeight(getMaxHeight());
        }
        pane.setCollapsible(collapse);

        setStyling(pane);

        if (alignment != null) {
            layout.setAlignment(alignment);
        }
        layout.setSpacing(spacing);
        Pane vBox = layout.render();
        pane.setContent(vBox);

        LinkedList<Node> order = buildBase();
        order.add(pane);

        return new AbstractMap.SimpleEntry<>(pane, order);
    }
}
