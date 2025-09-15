package me.piitex.engine.layouts;


import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.*;
import me.piitex.engine.Element;
import me.piitex.engine.hanlders.events.LayoutClickEvent;

public class TitledLayout extends VerticalLayout {
    private String title;
    private boolean collapse = true;
    private boolean expanded = true;
    private final TitledPane titledPane;

    public TitledLayout(String title, double width, double height) {
        super(width, height);
        this.title = title;
        this.titledPane = new TitledPane(title, getPane());
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
        titledPane.setCollapsible(collapse);
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
        titledPane.setExpanded(expanded);
    }


    public void setSpacing(double spacing) {
        VBox vBox = (VBox) getNode();
        vBox.setSpacing(spacing);
    }

    public TitledPane getTitledPane() {
        return titledPane;
    }

    @Override
    public Node render() {
        titledPane.setCollapsible(collapse);
        titledPane.setExpanded(expanded);

        VBox pane = (VBox) super.render();
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

        for (Element element : getElements().values()) {
            Node child = element.assemble();
            if (pane.getChildren().contains(child)) {
                int index = pane.getChildren().indexOf(child);
                pane.getChildren().set(index, child);
            } else {
                pane.getChildren().add(element.assemble());
            }
        }


        if (getClickEvent() != null) {
            pane.setOnMousePressed(mouseEvent -> {
                getClickEvent().onLayoutClick(new LayoutClickEvent(mouseEvent, this));
            });
        } else {
            pane.setOnMousePressed(null);
        }


        setStyling(pane);
        titledPane.setContent(pane);
        return titledPane;
    }
}
