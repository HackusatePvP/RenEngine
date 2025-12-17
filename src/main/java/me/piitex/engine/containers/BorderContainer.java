package me.piitex.engine.containers;

import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import me.piitex.engine.Element;
import me.piitex.engine.hanlders.events.ContainerClickEvent;

public class BorderContainer extends Container {
    private final BorderPane pane;

    private Element top, bottom, right, left, center;

    public BorderContainer(double x, double y, double width, double height, int index) {
        BorderPane tempPane = new BorderPane();
        this.pane = tempPane;
        super(tempPane, x, y, width, height, index);
    }

    public BorderContainer(double x, double y, double width, double height) {
        this(x, y, width, height, 0);
    }

    public BorderContainer(double width, double height) {
        this(0, 0, width, height, 0);
    }

    public BorderContainer(double width, double height, int index) {
        this(0, 0, width, height, index);
    }

    public BorderPane getPane() {
        return pane;
    }

    public Element getTop() {
        return top;
    }

    public void setTop(Element top) {
        this.top = top;
        pane.setTop(top.assemble());
    }

    public Element getBottom() {
        return bottom;
    }

    public void setBottom(Element bottom) {
        this.bottom = bottom;
        pane.setBottom(bottom.assemble());
    }

    public Element getRight() {
        return right;
    }

    public void setRight(Element right) {
        this.right = right;
        pane.setRight(right.assemble());
    }

    public Element getLeft() {
        return left;
    }

    public void setLeft(Element left) {
        this.left = left;
        pane.setLeft(left.assemble());
    }

    public Element getCenter() {
        return center;
    }

    public void setCenter(Element center) {
        this.center = center;
        pane.setCenter(center.assemble());
    }

    @Override
    public Node build() {
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

        setStyling(pane);

        if (getOnClick() != null) {
            pane.setOnMouseClicked(event -> getOnClick().onClick(new ContainerClickEvent(this)));
        }

        return pane;
    }
}
