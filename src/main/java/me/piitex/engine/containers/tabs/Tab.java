package me.piitex.engine.containers.tabs;

import javafx.scene.layout.Pane;
import me.piitex.engine.Renderer;

public class Tab extends Renderer {
    private final String text;
    // The tab box has a height of 20. This is why this is set to 20, so the text isn't hidden behind the tab box.
    private double x = 0, y = 20;

    public Tab(String text) {
        this.text = text;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public javafx.scene.control.Tab render() {
        Pane content = new Pane();
        content.setTranslateX(getX());
        content.setTranslateY(getY());
        content.setPrefSize(getWidth(), getHeight());
        javafx.scene.control.Tab jTab = new javafx.scene.control.Tab(text);
        jTab.setContent(content);

        content.getChildren().addAll(buildBase());
        setStyling(content);
        return jTab;
    }
}
