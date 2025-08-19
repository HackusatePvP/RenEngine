package me.piitex.engine.overlays;

import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import me.piitex.engine.hanlders.events.ComboBoxSelectEvent;
import me.piitex.engine.overlays.events.IComboSelect;

import java.util.List;

public class ComboBoxOverlay extends Overlay implements Region {
    private final ComboBox<String> comboBox;
    private String selected;
    private final List<String> items;
    private double width, height, prefWidth, prefHeight, maxWidth, maxHeight;
    private double scaleWidth, scaleHeight;
    private IComboSelect comboSelect;
    private Node node;

    private String defaultItem;

    public ComboBoxOverlay(List<String> items) {
        this.comboBox = new ComboBox<>();
        this.items = items;
    }

    public ComboBoxOverlay(List<String> items, double width, double height) {
        this.comboBox = new ComboBox<>();
        this.items = items;
        this.width = width;
        this.height = height;
    }

    public ComboBoxOverlay(String[] items, double width, double height) {
        this.comboBox = new ComboBox<>();
        this.items = List.of(items);
        this.width = width;
        this.height = height;
    }

    public ComboBoxOverlay(List<String> items, double width, double height, double x, double y) {
        this.comboBox = new ComboBox<>();
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
        comboBox.getSelectionModel().select(defaultItem);
    }

    public List<String> getItems() {
        return items;
    }

    public String getSelected() {
        return comboBox.getSelectionModel().getSelectedItem();
    }

    public void setSelected(String selected) {
        this.selected = selected;
        comboBox.getSelectionModel().select(selected);
    }

    public Node getNode() {
        return node;
    }

    @Override
    public Node render() {
        comboBox.setTranslateX(getX());
        comboBox.setTranslateY(getY());

        if (width > 0) {
            comboBox.setPrefWidth(width);
        }
        if (height > 0) {
            comboBox.setPrefHeight(height);
        }

        comboBox.getItems().setAll(items);
        if (defaultItem != null) {
            comboBox.getSelectionModel().select(defaultItem);
        } else if (!items.isEmpty()) {
            comboBox.getSelectionModel().selectFirst();
            if (getComboSelect() != null) {
                getComboSelect().onItemSelect(new ComboBoxSelectEvent(comboBox,this, items.getFirst()));
            }
        }

        comboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (getComboSelect() != null) {
                getComboSelect().onItemSelect(new ComboBoxSelectEvent(comboBox,this, newValue));
            }
            this.selected = newValue;
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
        comboBox.setMinWidth(w);
    }

    @Override
    public void setHeight(double h) {
        this.height = h;
        comboBox.setMinHeight(h);
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
        comboBox.setPrefWidth(w);
    }

    @Override
    public void setPrefHeight(double h) {
        this.prefHeight = h;
        comboBox.setPrefHeight(h);
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
        comboBox.setMaxWidth(w);
    }

    @Override
    public void setMaxHeight(double h) {
        this.maxHeight = h;
        comboBox.setMaxHeight(h);
    }
}
