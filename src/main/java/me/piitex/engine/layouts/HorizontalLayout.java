package me.piitex.engine.layouts;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import me.piitex.engine.Container;
import me.piitex.engine.Element;
import me.piitex.engine.hanlders.events.LayoutClickEvent;
import me.piitex.engine.overlays.Overlay;


public class HorizontalLayout extends Layout {
    private int spacing;

    public HorizontalLayout(double width, double height) {
        super(new HBox(), width, height);
    }

    public int getSpacing() {
        return spacing;
    }

    public void setSpacing(int spacing) {
        this.spacing = spacing;
    }

    @Override
    public Pane render(Container container) {
        // Clear
        HBox pane = (HBox) getPane();
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

        return getPane();
    }
}