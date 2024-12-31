package me.piitex.engine.overlays;

import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import me.piitex.engine.hanlders.events.ComboBoxSelectEvent;
import me.piitex.engine.overlays.events.IComboSelect;

import java.util.ArrayList;
import java.util.List;

public class ComboBoxOverlay extends Overlay implements Region {
    private List<String> items = new ArrayList<>();
    private double width, height;
    private double scaleWidth, scaleHeight;
    private IComboSelect comboSelect;

    public ComboBoxOverlay(List<String> items) {
        this.items = items;
    }

    public ComboBoxOverlay(List<String> items, double width, double height) {
        this.items = items;
        this.width = width;
        this.height = height;
    }

    public ComboBoxOverlay(List<String> items, double width, double height, double x, double y) {
        this.items = items;
        this.width = width;
        this.height = height;
        setX(x);
        setY(y);
    }

    @Override
    public Node render() {
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setTranslateX(getX());
        comboBox.setTranslateY(getY());

        if (width > 0) {
            comboBox.setPrefWidth(width);
        }
        if (height > 0) {
            comboBox.setPrefHeight(height);
        }

        comboBox.getItems().addAll(items);

        if (!items.isEmpty()) {
            comboBox.getSelectionModel().selectFirst();
            if (getComboSelect() != null) {
                getComboSelect().onItemSelect(new ComboBoxSelectEvent(this, items.getFirst()));
            }
        }

        comboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (getComboSelect() != null) {
                System.out.println("Handling combo select...");
                getComboSelect().onItemSelect(new ComboBoxSelectEvent(this, newValue));
            }
        });

        setInputControls(comboBox);

        return comboBox;
    }

    public void onItemSelect(IComboSelect iComboSelect) {
        this.comboSelect = iComboSelect;
    }

    public IComboSelect getComboSelect() {
        return comboSelect;
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
}
