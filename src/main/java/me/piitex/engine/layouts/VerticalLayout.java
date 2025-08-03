package me.piitex.engine.layouts;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.*;
import me.piitex.engine.hanlders.events.LayoutClickEvent;

public class VerticalLayout extends Layout {
    private double spacing = 10;

    public VerticalLayout(double width, double height) {
        super(new VBox(), width, height);
    }

    public double getSpacing() {
        return spacing;
    }

    public void setSpacing(double spacing) {
        this.spacing = spacing;
    }

    @Override
    public Node render() {
        // Clear
        VBox pane = (VBox) getView();
        pane.setSpacing(getSpacing());
        pane.setTranslateX(getX());
        pane.setTranslateY(getY());
        pane.getChildren().clear();
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
        if (getAlignment() != null) {
            pane.setAlignment(getAlignment());
        }
        if (getBackgroundColor() != null) {
            pane.setBackground(new Background(new BackgroundFill(getBackgroundColor(), CornerRadii.EMPTY, Insets.EMPTY)));
        }

        if (getClickEvent() != null) {
            pane.setOnMousePressed(mouseEvent -> {
                getClickEvent().onLayoutClick(new LayoutClickEvent(mouseEvent, this));
            });
        } else {
            pane.setOnMousePressed(null);
        }

        pane.getChildren().addAll(buildBase());
        setStyling(pane);
        return pane;
    }
}