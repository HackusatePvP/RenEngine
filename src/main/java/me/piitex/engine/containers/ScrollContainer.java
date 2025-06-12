package me.piitex.engine.containers;

import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import me.piitex.engine.Container;
import me.piitex.engine.layouts.Layout;

import java.util.AbstractMap;
import java.util.LinkedList;
import java.util.Map;

public class ScrollContainer extends Container {
    private final Layout layout;
    private ScrollPane scrollPane;
    private double xOffset, yOffset;
    private boolean horizontalScroll = true;
    private boolean verticalScroll = true;
    private boolean scrollWhenNeeded = true;

    public ScrollContainer(Layout layout, double x, double y, double width, double height) {
        super(x, y, width, height);
        this.layout = layout;
    }

    public double getXOffset() {
        return xOffset;
    }

    public void setXOffset(double xOffset) {
        this.xOffset = xOffset;
    }

    public double getYOffset() {
        return yOffset;
    }

    public void setYOffset(double yOffset) {
        this.yOffset = yOffset;
    }

    public boolean isHorizontalScroll() {
        return horizontalScroll;
    }

    public void setHorizontalScroll(boolean horizontalScroll) {
        this.horizontalScroll = horizontalScroll;
    }

    public boolean isVerticalScroll() {
        return verticalScroll;
    }

    public void setVerticalScroll(boolean verticalScroll) {
        this.verticalScroll = verticalScroll;
    }

    public boolean isScrollWhenNeeded() {
        return scrollWhenNeeded;
    }

    public void setScrollWhenNeeded(boolean scrollWhenNeeded) {
        this.scrollWhenNeeded = scrollWhenNeeded;
    }

    public ScrollPane getScrollPane() {
        return scrollPane;
    }

    @Override
    public Map.Entry<Node, LinkedList<Node>> build() {
        scrollPane = new ScrollPane();
        scrollPane.setTranslateX(getX());
        scrollPane.setTranslateY(getY());
        scrollPane.setPrefSize(getWidth(), getHeight());
        if (verticalScroll) {
            scrollPane.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.ALWAYS);
        } else if (scrollWhenNeeded) {
            scrollPane.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        } else {
            scrollPane.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
        }
        if (horizontalScroll) {
            scrollPane.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.ALWAYS);
        } else if (scrollWhenNeeded) {
            scrollPane.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        } else {
            scrollPane.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
        }

        // Build pane layout for the scroll content
        Pane pane = layout.render(this);
        scrollPane.setContent(pane);

        LinkedList<Node> order = buildBase();
        order.add(pane);

        // Offset overlays by 10
        if (xOffset > 0 || yOffset > 0) {
            order.forEach(node -> {
                node.setTranslateX(node.getTranslateX() + xOffset);
                node.setTranslateY(node.getTranslateX() + yOffset);
            });
        }

        return new AbstractMap.SimpleEntry<>(scrollPane, order);
    }
}