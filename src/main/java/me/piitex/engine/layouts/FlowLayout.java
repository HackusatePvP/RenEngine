package me.piitex.engine.layouts;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import me.piitex.engine.hanlders.events.LayoutClickEvent;

public class FlowLayout extends Layout {
    private int verticalSpacing;
    private int horizontalSpacing;
    private final FlowPane pane;

    public FlowLayout(double width, double height) {
        super(new FlowPane(), width, height);
        this.pane = (FlowPane) getView();
    }

    public int getVerticalSpacing() {
        return verticalSpacing;
    }

    public void setVerticalSpacing(int verticalSpacing) {
        this.verticalSpacing = verticalSpacing;
    }

    public int getHorizontalSpacing() {
        return horizontalSpacing;
    }

    public void setHorizontalSpacing(int horizontalSpacing) {
        this.horizontalSpacing = horizontalSpacing;
    }

    @Override
    public Node render() {
        VBox.setVgrow(pane, Priority.ALWAYS);
        HBox.setHgrow(pane, Priority.ALWAYS);
        pane.setVgap(getVerticalSpacing());
        pane.setHgap(getHorizontalSpacing());
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
            pane.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
                getClickEvent().onLayoutClick(new LayoutClickEvent(mouseEvent,this));
            });
        }

        setStyling(pane);
        return pane;
    }
}
