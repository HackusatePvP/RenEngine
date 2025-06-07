package me.piitex.engine.containers;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import me.piitex.engine.Container;
import me.piitex.engine.Window;
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
 *       window.render();
 *     }
 * </pre>
 * @see Container
 * @see Layout
 * @see Overlay
 * @see Window
 */
public class EmptyContainer extends Container {

    public EmptyContainer(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    public EmptyContainer(double x, double y, double width, double height, int index) {
        super(x, y, width, height, index);
    }

    public EmptyContainer(double width, double height) {
        super(0, 0, width, height);
    }

    public EmptyContainer(double width, double height, int index) {
        super(0, 0, width, height, index);
    }

    @Override
    public Map.Entry<Node, LinkedList<Node>> build() {
        Pane pane = new Pane();
        pane.setTranslateX(getX());
        pane.setTranslateY(getY());
        pane.setPrefSize(getWidth(), getHeight());

        LinkedList<Node> order = buildBase();

        // Return loworder because the other orders are added onto the low order.
        return new AbstractMap.SimpleEntry<>(pane, order);
    }
}