package me.piitex.engine.overlays;

import javafx.beans.binding.Bindings;
import javafx.scene.Node;
import javafx.scene.control.ProgressBar;

public class ProgressBarOverlay extends Overlay implements Region {
    private double width, height, prefWidth, prefHeight, maxWidth, maxHeight;
    private double scaleWidth, scaleHeight;
    private ProgressBar progressBar;

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
    public double getScaleWidth() {
        return scaleWidth;
    }

    @Override
    public void setScaleWidth(double w) {
        this.scaleWidth = w;
    }

    @Override
    public double getScaleHeight() {
        return scaleHeight;
    }

    @Override
    public void setScaleHeight(double h) {
        this.scaleHeight = h;
    }



    @Override
    public Node render() {
        progressBar = new ProgressBar(0);
        progressBar.progressProperty().bind(Bindings.createDoubleBinding(() -> -1d));

        progressBar.setTranslateX(getX());
        progressBar.setTranslateY(getY());

        if (getWidth() > 0) {
            progressBar.setMinWidth(getWidth());
        }
        if (getPrefWidth() > 0) {
            progressBar.setPrefWidth(getPrefWidth());
        }
        if (getMaxWidth() > 0) {
            progressBar.setMaxWidth(getMaxWidth());
        }

        if (getHeight() > 0) {
            progressBar.setMinHeight(getHeight());
        }
        if (getPrefHeight() > 0) {
            progressBar.setPrefHeight(getPrefHeight());
        }
        if (getMaxHeight() > 0) {
            progressBar.setMaxHeight(getMaxHeight());
        }

        progressBar.getStyleClass().addAll(getStyles());

        return progressBar;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }
}
