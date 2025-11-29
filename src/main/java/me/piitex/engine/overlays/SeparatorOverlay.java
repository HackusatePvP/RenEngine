package me.piitex.engine.overlays;

import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Separator;

public class SeparatorOverlay extends Overlay implements Region {
    private final Separator separator;
    private Orientation orientation;
    private double width, height, prefWidth, prefHeight, maxWidth, maxHeight;
    private double scaleWidth, scaleHeight;

    public SeparatorOverlay(Orientation orientation) {
        this.separator = new Separator(orientation);
        this.orientation = orientation;
        setNode(separator);
    }

    public Orientation getOrientation(Orientation orientation) {
        return orientation;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
        separator.setOrientation(orientation);
    }

    @Override
    public double getWidth() {
        return width;
    }

    @Override
    public double getHeight() {
        return height;
    }

    @Override
    public void setWidth(double w) {
        this.width = w;
        separator.setMinWidth(w);
    }

    @Override
    public void setHeight(double h) {
        this.height = h;
        separator.setMinHeight(h);
    }

    @Override
    public double getPrefWidth() {
        return prefWidth;
    }

    @Override
    public double getPrefHeight() {
        return prefHeight;
    }

    @Override
    public void setPrefWidth(double w) {
        this.prefWidth = w;
        separator.setPrefWidth(w);
    }

    @Override
    public void setPrefHeight(double h) {
        this.prefHeight = h;
        separator.setPrefHeight(h);
    }

    @Override
    public double getMaxWidth() {
        return maxWidth;
    }

    @Override
    public double getMaxHeight() {
        return maxHeight;
    }

    @Override
    public void setMaxWidth(double w) {
        separator.setMaxWidth(w);
    }

    @Override
    public void setMaxHeight(double h) {
        this.maxHeight = h;
        separator.setMaxHeight(h);
    }

    @Override
    public void setMaxSize(double w, double h) {
        this.maxWidth = w;
        this.maxHeight = h;
        separator.setMaxSize(w, h);
    }

    @Override
    public Node render() {
        separator.getStyleClass().addAll(getStyles());
        separator.setTranslateX(getX());
        separator.setTranslateY(getY());
        if (getWidth() > 0) {
            separator.setMinWidth(width);
        }

        if (getHeight() > 0) {
            separator.setMinHeight(height);
        }

        if (getPrefWidth() > 0) {
            separator.setPrefWidth(prefWidth);
        }

        if (getPrefHeight() > 0) {
            separator.setPrefHeight(prefHeight);
        }

        if (getMaxWidth() > 0) {
            separator.setMaxWidth(maxWidth);
        }

        if (getMaxHeight() > 0) {
            separator.setMaxHeight(maxHeight);
        }
        return separator;
    }
}
