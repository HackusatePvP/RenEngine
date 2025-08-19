package me.piitex.engine.overlays;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class BoxOverlay extends Overlay implements Region {
    private double width, height, prefWidth, prefHeight, maxWidth, maxHeight;
    private double scaleWidth, scaleHeight;
    private Color fillColor;
    private Color strokeColor = Color.WHITE;

    public BoxOverlay(double width, double height) {
        this.width = width;
        this.height = height;
    }

    public BoxOverlay(double width, double height, double x, double y) {
        this.width = width;
        this.height = height;
        setX(x);
        setY(y);
    }

    public BoxOverlay(double width, double height, Color color) {
        this.width = width;
        this.height = height;
        this.fillColor = color;
    }
    public BoxOverlay(double width, double height, double x, double y, Color color) {
        this.width = width;
        this.height = height;
        this.strokeColor = color;
        setX(x);
        setY(y);
    }

    public Color getFillColor() {
        return fillColor;
    }

    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
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
    }

    @Override
    public void setHeight(double h) {
        this.height = h;
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
    }

    @Override
    public void setPrefHeight(double h) {
        this.prefHeight = h;
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
        this.maxWidth = w;
    }

    @Override
    public void setMaxHeight(double h) {
        this.maxHeight = h;
    }

    @Override
    public Node render() {
        Rectangle rectangle = new Rectangle(width, height);
        rectangle.setX(getX());
        rectangle.setY(getY());
        if (getFillColor() != null) {
            rectangle.setFill(getFillColor());
        }
        rectangle.setStroke(strokeColor);

        setInputControls(rectangle);
        return rectangle;
    }
}
