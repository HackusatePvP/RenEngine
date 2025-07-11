package me.piitex.engine.overlays;

import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import me.piitex.engine.hanlders.events.ComboBoxSelectEvent;
import me.piitex.engine.overlays.events.IComboSelect;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ChoiceBoxOverlay extends Overlay implements Region {
    private List<String> items = new ArrayList<>();
    private double width, height, prefWidth, prefHeight, maxWidth, maxHeight;
    private double scaleWidth, scaleHeight;
    private IComboSelect comboSelect;
    private Node node;

    private String defaultItem;

    public ChoiceBoxOverlay(List<String> items) {
        this.items = items;
    }

    public ChoiceBoxOverlay(List<String> items, double width, double height) {
        this.items = items;
        this.width = width;
        this.height = height;
    }

    public ChoiceBoxOverlay(String[] items, double width, double height) {
        this.items = List.of(items);
        this.width = width;
        this.height = height;
    }

    public ChoiceBoxOverlay(List<String> items, double width, double height, double x, double y) {
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
        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        choiceBox.setTranslateX(getX());
        choiceBox.setTranslateY(getY());

        if (getWidth() > 0 || getHeight() > 0) {
            choiceBox.setMinSize(width, height);
        }

        choiceBox.getItems().addAll(items);
        if (defaultItem != null) {
            choiceBox.getSelectionModel().select(defaultItem);
        } else if (!items.isEmpty()) {
            choiceBox.getSelectionModel().selectFirst();
        }
        if (getComboSelect() != null) {
            choiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                getComboSelect().onItemSelect(new ComboBoxSelectEvent(choiceBox,this, newValue));
            });
        }

        setInputControls(choiceBox);
        this.node = choiceBox;
        return choiceBox;
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

}
