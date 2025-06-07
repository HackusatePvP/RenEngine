package me.piitex.engine.layouts;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import me.piitex.engine.Container;
import me.piitex.engine.Element;
import me.piitex.engine.hanlders.events.LayoutClickEvent;
import me.piitex.engine.overlays.Overlay;

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
    public Pane render(Container container) {
        // Clear
        VBox pane = (VBox) getPane();
        pane.setSpacing(getSpacing());
        pane.setTranslateX(getX());
        pane.setTranslateY(getY());
        pane.getChildren().clear();
        pane.setPrefWidth(getWidth());
        pane.setPrefHeight(getHeight());
        if (getAlignment() != null) {
            pane.setAlignment(getAlignment());
        }
        if (getBackground() != null) {
            pane.setBackground(new Background(new BackgroundFill(getBackground(), CornerRadii.EMPTY, Insets.EMPTY)));
        }

        if (getClickEvent() != null) {
            pane.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
                getClickEvent().onLayoutClick(new LayoutClickEvent(this));
            });
        }

        for (Element element : getElements().values()) {
            if (element instanceof Overlay overlay) {
                Node node = overlay.render();
                if (getOffsetX() > 0 || getOffsetY() > 0) {
                    node.setTranslateX(node.getTranslateX() + getOffsetX());
                    node.setTranslateY(node.getTranslateY() + getOffsetY());
                }
                pane.getChildren().add(node);
            } else if (element instanceof Layout layout) {
                if ((layout.getOffsetX() == 0 && getOffsetX() != 0) || (layout.getOffsetY() == 0 && getOffsetY() != 0)) {
                    System.out.println("Updating offsets...");
                    layout.setOffsetX(getOffsetX());
                    layout.setOffsetY(getOffsetY());
                }
                pane.getChildren().add(layout.render(container));
            } else {
                System.out.println("Element type not supported for layouts.");
            }
        }

        return pane;
    }
}