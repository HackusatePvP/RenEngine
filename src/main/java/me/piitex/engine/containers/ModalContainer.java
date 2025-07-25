package me.piitex.engine.containers;

import atlantafx.base.layout.ModalBox;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import me.piitex.engine.Container;
import me.piitex.engine.Element;
import me.piitex.engine.hanlders.events.LayoutRenderEvent;
import me.piitex.engine.layouts.Layout;
import me.piitex.engine.overlays.Overlay;

import java.util.AbstractMap;
import java.util.LinkedList;
import java.util.Map;

public class ModalContainer extends Container {
    private Element content;

    public ModalContainer(Element content, double width, double height) {
        super(0, 0, width, height);
        this.content = content;
    }


    public ModalContainer(Element content, double x, double y, double width, double height) {
        super(x, y, width, height);
        this.content = content;
    }

    public ModalContainer(Element content, double x, double y, double width, double height, int index) {
        super(x, y, width, height, index);
        this.content = content;
    }

    public Element getContent() {
        return content;
    }

    public void setContent(Element content) {
        this.content = content;
    }

    @Override
    public Map.Entry<Node, LinkedList<Node>> build() {
        ModalBox modalBox = new ModalBox();
        modalBox.setTranslateX(getX());
        modalBox.setTranslateY(getY());
        if (getWidth() > 0) {
            modalBox.setMinWidth(getWidth());
        }
        if (getHeight() > 0) {
            modalBox.setMinHeight(getHeight());
        }

        if (getPrefWidth() > 0) {
            modalBox.setPrefWidth(getPrefWidth());
        }
        if (getPrefHeight() > 0) {
            modalBox.setPrefHeight(getPrefHeight());
        }

        if (getMaxWidth() > 0) {
            modalBox.setMaxWidth(getMaxWidth());
        }
        if (getMaxHeight() > 0) {
            modalBox.setMaxHeight(getMaxHeight());
        }
        if (content != null) {
            Node node = null;
            if (content instanceof Overlay overlay) {
                node = overlay.render();
            }
            if (content instanceof Layout layout) {
                node = layout.render();
                LayoutRenderEvent event = new LayoutRenderEvent(layout.getPane(), layout);
                layout.getRenderEvents().forEach(iLayoutRender -> iLayoutRender.onLayoutRender(event));
                if (node instanceof VBox vBox) {
                    vBox.setPadding(new Insets(16));
                }
                if (node instanceof HBox box) {
                    box.setPadding(new Insets(16));
                }
            }
            if (content instanceof Container container) {
                node = container.build().getKey();
                if (node instanceof Pane pane) {
                    pane.setPadding(new Insets(16));
                }
            }
            if (node != null) {
                AnchorPane.setTopAnchor(node, 0d);
                AnchorPane.setRightAnchor(node, 0d);
                AnchorPane.setBottomAnchor(node, 0d);
                AnchorPane.setLeftAnchor(node, 0d);

                modalBox.addContent(node);
            }
        }

        LinkedList<Node> order = buildBase();

        modalBox.getStyleClass().addAll(getStyles());

        setView(modalBox);

        return new AbstractMap.SimpleEntry<>(modalBox, order);
    }
}
