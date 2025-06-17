package me.piitex.engine.layouts;

import javafx.geometry.Insets;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import me.piitex.engine.Container;
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
    public Pane render() {
        // Clear
        VBox pane = (VBox) getPane();
        pane.setSpacing(getSpacing());
        pane.setTranslateX(getX());
        pane.setTranslateY(getY());
        pane.getChildren().clear();
        pane.setPrefSize(getWidth(), getHeight());
        pane.setMinSize(getWidth(), getHeight());
        if (getAlignment() != null) {
            pane.setAlignment(getAlignment());
        }
        if (getBackgroundColor() != null) {
            pane.setBackground(new Background(new BackgroundFill(getBackgroundColor(), CornerRadii.EMPTY, Insets.EMPTY)));
        }

        if (getClickEvent() != null) {
            pane.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
                getClickEvent().onLayoutClick(new LayoutClickEvent(this));
            });
        }

        pane.getChildren().addAll(buildBase());
        setStyling(pane);
        return pane;
    }
}