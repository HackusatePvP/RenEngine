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
    private final ModalBox modalBox;
    private Element content;

    public ModalContainer(double width, double height) {
        super(new ModalBox(), 0, 0, width, height);
        this.modalBox = (ModalBox) getView();
    }

    public ModalContainer(double x, double y, double width, double height) {
        super(new ModalBox(), x, y, width, height);
        this.modalBox = (ModalBox) getView();
    }

    public ModalContainer(double x, double y, double width, double height, int index) {
        super(new ModalBox(), x, y, width, height, index);
        this.modalBox = (ModalBox) getView();
    }

    public Element getContent() {
        return content;
    }

    public void setContent(Element content) {
        this.content = content;
        Node node = content.assemble();
        AnchorPane.setTopAnchor(node, 0d);
        AnchorPane.setRightAnchor(node, 0d);
        AnchorPane.setBottomAnchor(node, 0d);
        AnchorPane.setLeftAnchor(node, 0d);
        modalBox.addContent(content.assemble());
    }

    @Override
    public Node build() {
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
            Node node = content.assemble();
            if (!modalBox.getChildren().contains(node)) {
                System.out.println("Adding to node!");
                AnchorPane.setTopAnchor(node, 0d);
                AnchorPane.setRightAnchor(node, 0d);
                AnchorPane.setBottomAnchor(node, 0d);
                AnchorPane.setLeftAnchor(node, 0d);
                modalBox.addContent(node);
            }
        }
        modalBox.getStyleClass().addAll(getStyles());

        setView(modalBox);

        return modalBox;
    }
}
