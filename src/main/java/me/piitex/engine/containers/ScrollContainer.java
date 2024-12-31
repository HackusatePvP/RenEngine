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

    public ScrollContainer(Layout layout, double x, double y) {
        super(x, y, layout.getWidth(), layout.getHeight());
        this.layout = layout;
    }

    @Override
    public Map.Entry<Node, LinkedList<Node>> build() {
        scrollPane = new ScrollPane();
        scrollPane.setPrefSize(getWidth(), getHeight());
        scrollPane.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.ALWAYS);

        // Build pane layout for the scroll content
        Pane pane = layout.getPane();
        scrollPane.setContent(pane);


        LinkedList<Node> lowOrder = new LinkedList<>();
        LinkedList<Node> normalOrder = new LinkedList<>();
        LinkedList<Node> highOrder = new LinkedList<>();

        lowOrder.add(layout.render(this));

        buildBase(lowOrder, normalOrder, highOrder);

        return new AbstractMap.SimpleEntry<>(scrollPane, lowOrder);
    }
}
