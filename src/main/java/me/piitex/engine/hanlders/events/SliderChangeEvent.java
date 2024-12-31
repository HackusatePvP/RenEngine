package me.piitex.engine.hanlders.events;

import me.piitex.engine.overlays.SliderOverlay;

public class SliderChangeEvent extends Event {
    private final SliderOverlay sliderOverlay;
    private double value;

    public SliderChangeEvent(SliderOverlay sliderOverlay, double value) {
        this.sliderOverlay = sliderOverlay;
        this.value = value;
    }

    public SliderOverlay getSliderOverlay() {
        return sliderOverlay;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
