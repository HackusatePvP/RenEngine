package me.piitex.engine.layouts;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import me.piitex.engine.Container;
import me.piitex.engine.overlays.Overlay;

public class VerticalLayout extends Layout {
    private double spacing = 10;
    private boolean scroll = false;

    public VerticalLayout(double width, double height) {
        super(new VBox(), width, height);
    }

    public double getSpacing() {
        return spacing;
    }

    public void setSpacing(double spacing) {
        this.spacing = spacing;
    }

    public boolean isScroll() {
        return scroll;
    }

    public void setScroll(boolean scroll) {
        this.scroll = scroll;
    }

    @Override
    public Node render(Container container) {
        // Clear
        VBox pane = (VBox) getPane();
        if (isScroll())
            pane.setAlignment(Pos.TOP_LEFT);
        pane.setSpacing(getSpacing());
        pane.setTranslateX(getX());
        pane.setTranslateY(getY());
        pane.getChildren().clear();

        for (Overlay overlay : getOverlays()) {
            Node node = overlay.render();
            pane.getChildren().add(node);
        }

        for (Layout layout : getChildLayouts()) {
            pane.getChildren().add(layout.render(container));
        }

        if (scroll) {
            ScrollPane scrollPane = new ScrollPane(pane);
            scrollPane.setPrefSize(getWidth(), getHeight());
            scrollPane.setTranslateX(getX());
            scrollPane.setTranslateY(getY());
            return scrollPane;
        }

        return getPane();
    }
}
