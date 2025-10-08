package me.piitex.engine.containers;

import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import me.piitex.engine.Container;
import me.piitex.engine.Element;
import me.piitex.engine.hanlders.events.ContainerClickEvent;

public class BorderContainer extends Container {
    private final BorderPane pane;

    private Element top, bottom, right, left, center, border;

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
    }

    public Element getBottom() {
        return bottom;
    }

    public void setBottom(Element bottom) {
        this.bottom = bottom;
    }

    public Element getRight() {
        return right;
    }

    public void setRight(Element right) {
        this.right = right;
    }

    public Element getLeft() {
        return left;
    }

    public void setLeft(Element left) {
        this.left = left;
    }

    public Element getCenter() {
        return center;
    }

    public void setCenter(Element center) {
        this.center = center;
    }

    public Element getBorder() {
        return border;
    }

    public void setBorder(Element border) {
        this.border = border;
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

        if (top != null) {
            pane.setTop(top.assemble());
        }
        if (bottom != null) {
            pane.setBottom(bottom.assemble());
        }
        if (right != null) {
            pane.setRight(right.assemble());
        }
        if (left != null) {
            pane.setLeft(left.assemble());
        }
        if (center != null) {
            pane.setCenter(center.assemble());
        }
        if (border != null) {
            pane.setBottom(border.assemble());
        }

        setStyling(pane);

        if (getOnClick() != null) {
            pane.setOnMouseClicked(event -> getOnClick().onClick(new ContainerClickEvent(this)));
        }

        return pane;
    }
}
