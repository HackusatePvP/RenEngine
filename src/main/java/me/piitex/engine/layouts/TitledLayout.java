package me.piitex.engine.layouts;


import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.*;
import me.piitex.engine.hanlders.events.LayoutClickEvent;

public class TitledLayout extends VerticalLayout {
    private String title;
    private boolean collapse = true;
    private boolean expanded = true;

    public TitledLayout(String title, double width, double height) {
        super(width, height);
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isCollapse() {
        return collapse;
    }

    public void setCollapse(boolean collapse) {
        this.collapse = collapse;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    @Override
    public Node render() {
        TitledPane titledPane = new TitledPane(title, getView());
        titledPane.setCollapsible(collapse);
        titledPane.setExpanded(expanded);

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
        return titledPane;
    }
}
