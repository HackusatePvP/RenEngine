package me.piitex.engine.overlays;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.ProgressBar;

public class ProgressBarOverlay extends Overlay implements Region {
    private double width, height, prefWidth, prefHeight, maxWidth, maxHeight;
    private double scaleWidth, scaleHeight;
    private final ProgressBar progressBar;

    public ProgressBarOverlay() {
        this.progressBar = new ProgressBar();
    }

    public void bind(ObservableValue<? extends Number> binding) {
        progressBar.progressProperty().bind(binding);
    }

    @Override
    public Node render() {
        progressBar.setTranslateX(getX());
        progressBar.setTranslateY(getY());

        if (getWidth() > 0 || getHeight() > 0) {
            progressBar.setMinSize(width, height);
        }
        if (getPrefWidth() > 0 || getPrefHeight() > 0) {
            progressBar.setPrefSize(getPrefWidth(), getPrefHeight());
        }
        if (getMaxWidth() > 0 || getMaxHeight() > 0) {
            progressBar.setMaxSize(getMaxWidth(), getMaxHeight());
        }

        progressBar.getStyleClass().addAll(getStyles());

        return progressBar;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
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
        progressBar.setMinWidth(w);
    }

    @Override
    public void setHeight(double h) {
        this.height = h;
        progressBar.setMinHeight(h);
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
        progressBar.setPrefWidth(w);
    }

    @Override
    public void setPrefHeight(double h) {
        this.prefHeight = h;
        progressBar.setPrefHeight(h);
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
        progressBar.setMaxWidth(w);
    }

    @Override
    public void setMaxHeight(double h) {
        this.maxHeight = h;
        progressBar.setMaxHeight(h);
    }
}
