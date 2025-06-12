package me.piitex.engine.overlays;

import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import me.piitex.engine.hanlders.events.ComboBoxSelectEvent;
import me.piitex.engine.overlays.events.IComboSelect;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ComboBoxOverlay extends Overlay implements Region {
    private List<String> items = new ArrayList<>();
    private double width, height;
    private double scaleWidth, scaleHeight;
    private IComboSelect comboSelect;
    private Node node;

    private String defaultItem;

    public ComboBoxOverlay(List<String> items) {
        this.items = items;
    }

    public ComboBoxOverlay(List<String> items, double width, double height) {
        this.items = items;
        this.width = width;
        this.height = height;
    }

    public ComboBoxOverlay(String[] items, double width, double height) {
        this.items = List.of(items);
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

    public String getDefaultItem() {
        return defaultItem;
    }

    public void setDefaultItem(String defaultItem) {
        this.defaultItem = defaultItem;
    }

    public void setItems(LinkedList<String> items) {
        this.items = items;
        if (node != null) {
            ComboBox<String> comboBox = (ComboBox<String>) node;
        }
    }

    public List<String> getItems() {
        return items;
    }


    public Node getNode() {
        return node;
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
        if (defaultItem != null) {
            comboBox.getSelectionModel().select(defaultItem);
        } else if (!items.isEmpty()) {
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
        this.node = comboBox;
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
