package me.piitex.engine.hanlders.events;

import me.piitex.engine.overlays.SliderOverlay;

public class SliderChangeEvent extends Event {
    private final SliderOverlay sliderOverlay;
    private final double newValue;
    private final double oldValue;

    public SliderChangeEvent(SliderOverlay sliderOverlay, double newValue, double oldValue) {
        this.sliderOverlay = sliderOverlay;
        this.newValue = newValue;
        this.oldValue = oldValue;
    }

    public SliderOverlay getSliderOverlay() {
        return sliderOverlay;
    }

    public double getNewValue() {
        return newValue;
    }

    public double getOldValue() {
        return oldValue;
    }
}
