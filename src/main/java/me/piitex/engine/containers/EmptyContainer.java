package me.piitex.engine.containers;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import me.piitex.engine.Container;
import me.piitex.engine.Window;
import me.piitex.engine.hanlders.events.ContainerClickEvent;
import me.piitex.engine.layouts.Layout;
import me.piitex.engine.overlays.Overlay;

import java.util.AbstractMap;
import java.util.LinkedList;
import java.util.Map;

// A default container which contains no special layout. Completely normal container.
/**
 * The EmptyContainer is a default {@link Container} which houses {@link Overlay}s, {@link Layout}s, and sub-containers.
 * The container must be added to a {@link Window} to be rendered.
 * <pre>
 *     {@code
 *       EmptyContainer container = new EmptyContainer(x, y, width, height, index);
 *       window.addContainer(container);
 *     }
 * </pre>
 * @see Container
 * @see Layout
 * @see Overlay
 * @see Window
 */
public class EmptyContainer extends Container {
    private final Pane pane;

    public EmptyContainer(double x, double y, double width, double height) {
        super(new Pane(), x, y, width, height);
        this.pane = (Pane) getNode();
    }

    public EmptyContainer(double x, double y, double width, double height, int index) {
        super(new Pane(), x, y, width, height, index);
        this.pane = (Pane) getNode();
    }

    public EmptyContainer(double width, double height) {
        super(new Pane(), 0, 0, width, height);
        this.pane = (Pane) getNode();
    }

    public EmptyContainer(double width, double height, int index) {
        super(new Pane(), 0, 0, width, height, index);
        this.pane = (Pane) getNode();
    }

    public Pane getPane() {
        return pane;
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