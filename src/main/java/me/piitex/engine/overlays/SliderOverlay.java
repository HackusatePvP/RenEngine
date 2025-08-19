package me.piitex.engine.overlays;

import javafx.scene.Node;
import javafx.scene.control.Slider;
import me.piitex.engine.hanlders.events.SliderChangeEvent;
import me.piitex.engine.overlays.events.ISliderChange;

import java.io.File;
import java.net.MalformedURLException;

public class SliderOverlay extends Overlay implements Region {
    private final Slider slider;
    private double width, height, prefWidth, prefHeight, maxWidth, maxHeight;
    private double scaleWidth = 1, scaleHeight = 1;
    private double maxValue, minValue, currentValue;
    private double blockIncrement;

    private ISliderChange sliderChange;

    public SliderOverlay(double minValue, double maxValue, double currentValue) {
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.currentValue = currentValue;
        this.slider = new Slider(minValue, maxValue, currentValue);
    }

    public SliderOverlay(double minValue, double maxValue, double currentValue, double x, double y, double width, double height) {
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.currentValue = currentValue;
        this.slider = new Slider(minValue, maxValue, currentValue);
        setX(x);
        setY(y);
    }

    public double getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
        slider.setMax(maxValue);
    }

    public double getMinValue() {
        return minValue;
    }

    public void setMinValue(double minValue) {
        this.minValue = minValue;
        slider.setMin(minValue);
    }

    public double getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(double currentValue) {
        this.currentValue = currentValue;
        slider.setValue(currentValue);
    }

    public double getBlockIncrement() {
        return blockIncrement;
    }

    public void setBlockIncrement(double blockIncrement) {
        this.blockIncrement = blockIncrement;
        slider.setBlockIncrement(blockIncrement);
    }

    public ISliderChange getSliderChange() {
        return sliderChange;
    }

    public void onSliderMove(ISliderChange sliderChange) {
        this.sliderChange = sliderChange;
    }

    @Override
    public Node render() {
        if (getWidth() > 0) {
            slider.setMinWidth(width);
        }

        if (getHeight() > 0) {
            slider.setMinHeight(height);
        }

        if (getPrefWidth() > 0) {
            slider.setPrefWidth(prefWidth);
        }

        if (getPrefHeight() > 0) {
            slider.setPrefHeight(prefHeight);
        }

        if (getMaxWidth() > 0) {
            slider.setMaxWidth(maxWidth);
        }

        if (getMaxHeight() > 0) {
            slider.setMaxHeight(maxHeight);
        }
        slider.setTranslateX(getX());
        slider.setTranslateY(getY());
        slider.setBlockIncrement(blockIncrement);
        // To design sliders we NEED a css file which contains the styling. I'm not able to inline this via code which sucks.
        // Hopefully the slider gets improvements in JavaFX.

        // Check if they have css files
        File sliderCss = new File(System.getProperty("user.dir") + "/game/css/slider.css");
        if (!sliderCss.exists()) {
            // ERROR
        } else {
            try {
                slider.getStylesheets().add(sliderCss.toURI().toURL().toExternalForm());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        // Handle slider events
        slider.setOnMouseDragged(event -> {
            sliderChange.onSliderChange(new SliderChangeEvent(this, slider.getValue()));
        });

        setInputControls(slider);
        return slider;
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
        slider.setMinWidth(w);
    }

    @Override
    public void setHeight(double h) {
        this.height = h;
        slider.setMinHeight(h);
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
        slider.setPrefWidth(w);
    }

    @Override
    public void setPrefHeight(double h) {
        this.prefHeight = h;
        slider.setPrefHeight(h);
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
        slider.setMaxWidth(w);
    }

    @Override
    public void setMaxHeight(double h) {
        this.maxHeight = h;
        slider.setMaxHeight(h);
    }
}
