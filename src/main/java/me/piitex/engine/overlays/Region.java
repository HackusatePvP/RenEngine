package me.piitex.engine.overlays;

/**
 * Regions are overlays that have a shape. The region is used to define that shape.
 */
public interface Region {
    double getWidth();
    double getHeight();

    void setWidth(double w);
    void setHeight(double h);

    double getPrefWidth();
    double getPrefHeight();

    void setPrefWidth(double w);
    void setPrefHeight(double h);

    double getMaxWidth();
    double getMaxHeight();

    void setMaxWidth(double w);
    void setMaxHeight(double h);

    @Deprecated double getScaleWidth();
    @Deprecated void setScaleWidth(double w);
    @Deprecated double getScaleHeight();
    @Deprecated void setScaleHeight(double h);
}
