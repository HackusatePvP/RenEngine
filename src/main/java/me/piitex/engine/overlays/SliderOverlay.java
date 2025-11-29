package me.piitex.engine.overlays;

import atlantafx.base.controls.ProgressSliderSkin;
import javafx.scene.Node;
import javafx.scene.control.Slider;
import me.piitex.engine.hanlders.events.SliderChangeEvent;
import me.piitex.engine.overlays.events.ISliderChange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SliderOverlay extends Overlay implements Region {
    private final Slider slider;
    private double maxValue, minValue, currentValue;
    private double blockIncrement;
    private boolean tickLabels = false;
    private ProgressSliderSkin sliderSkin;
    private ISliderChange sliderChange;
    private double width, height, prefWidth, prefHeight, maxWidth, maxHeight;

    private static final Logger logger = LoggerFactory.getLogger(SliderOverlay.class);

    public SliderOverlay(double minValue, double maxValue, double currentValue) {
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.currentValue = currentValue;
        this.slider = new Slider(minValue, maxValue, currentValue);
        setNode(slider);
    }

    public SliderOverlay(double minValue, double maxValue, double currentValue, double x, double y, double width, double height) {
        this(minValue, maxValue, currentValue);
        setNode(slider);
        setX(x);
        setY(y);
        setWidth(width);
        setHeight(height);
    }

    public Slider getSlider() {
        return slider;
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

    public boolean isTickLabels() {
        return tickLabels;
    }

    public void setTickLabels(boolean tickLabels) {
        this.tickLabels = tickLabels;
    }

    public ProgressSliderSkin getSliderSkin() {
        return sliderSkin;
    }

    public void setSliderSkin(ProgressSliderSkin sliderSkin) {
        this.sliderSkin = sliderSkin;
    }

    public ISliderChange getSliderChange() {
        return sliderChange;
    }

    public void onSliderMove(ISliderChange sliderChange) {
        this.sliderChange = sliderChange;
    }

    @Override
    public Node render() {
        slider.setTranslateX(getX());
        slider.setTranslateY(getY());
        slider.setBlockIncrement(blockIncrement);
        slider.setSkin(new ProgressSliderSkin(slider));
        slider.valueProperty().addListener((_, oldValue, newValue) -> {
            if (getSliderChange() != null) {
                getSliderChange().onSliderChange(new SliderChangeEvent(this, newValue.doubleValue(), oldValue.doubleValue()));
            }
        });

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
        this.maxWidth = w;
        slider.setMaxWidth(w);
    }

    @Override
    public void setMaxHeight(double h) {
        this.maxHeight = h;
        slider.setMaxHeight(h);
    }

    @Override
    public void setMaxSize(double w, double h) {
        this.maxWidth = w;
        this.maxHeight = h;
        slider.setMaxSize(w, h);
    }
}
