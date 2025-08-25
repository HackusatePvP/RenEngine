package me.piitex.engine.containers;

import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import me.piitex.engine.Container;
import me.piitex.engine.hanlders.events.ContainerClickEvent;

public class StackContainer extends Container {
    private final StackPane pane;

    public StackContainer(double width, double height) {
        super(new StackPane(), 0, 0, width, height);
        this.pane = (StackPane) getView();
    }

    public StackPane getPane() {
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
