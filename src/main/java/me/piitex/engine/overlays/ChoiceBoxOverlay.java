package me.piitex.engine.overlays;

import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import me.piitex.engine.hanlders.events.ComboBoxSelectEvent;
import me.piitex.engine.overlays.events.IComboSelect;

import java.util.LinkedList;
import java.util.List;

public class ChoiceBoxOverlay extends Overlay implements Region {
    private final ChoiceBox<String> choiceBox;
    private String selected;
    private List<String> items;
    private double width, height, prefWidth, prefHeight, maxWidth, maxHeight;
    private double scaleWidth, scaleHeight;
    private IComboSelect comboSelect;
    private Node node;

    private String defaultItem;

    public ChoiceBoxOverlay(List<String> items) {
        this.choiceBox = new ChoiceBox<>();
        this.items = items;
    }

    public ChoiceBoxOverlay(List<String> items, double width, double height) {
        this.choiceBox = new ChoiceBox<>();
        this.items = items;
        this.width = width;
        this.height = height;
    }

    public ChoiceBoxOverlay(String[] items, double width, double height) {
        this.choiceBox = new ChoiceBox<>();
        this.items = List.of(items);
        this.width = width;
        this.height = height;
    }

    public ChoiceBoxOverlay(List<String> items, double width, double height, double x, double y) {
        this.choiceBox = new ChoiceBox<>();
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
        choiceBox.getSelectionModel().select(defaultItem);
    }

    public void setItems(LinkedList<String> items) {
        this.items = items;
        if (node != null) {
            ComboBox<String> comboBox = (ComboBox<String>) node;
        }
        choiceBox.getItems().setAll(items);
    }

    public List<String> getItems() {
        return items;
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
        choiceBox.getSelectionModel().select(selected);
    }

    public Node getNode() {
        return node;
    }

    @Override
    public Node render() {
        choiceBox.setTranslateX(getX());
        choiceBox.setTranslateY(getY());

        if (getWidth() > 0 || getHeight() > 0) {
            choiceBox.setMinSize(width, height);
        }
        if (getPrefWidth() > 0 || getPrefHeight() > 0) {
            choiceBox.setPrefSize(getPrefWidth(), getPrefHeight());
        }
        if (getMaxWidth() > 0 || getMaxHeight() > 0) {
            choiceBox.setMaxSize(getMaxWidth(), getMaxHeight());
        }

        choiceBox.getItems().setAll(items);
        setInputControls(choiceBox);
        this.node = choiceBox;

        if (defaultItem != null) {
            choiceBox.getSelectionModel().select(defaultItem);
        } else if (!items.isEmpty()) {
            choiceBox.getSelectionModel().selectFirst();
        }
        if (getComboSelect() != null) {
            choiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue == null) return;
                getComboSelect().onItemSelect(new ComboBoxSelectEvent(choiceBox,this, newValue));
            });
        }


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
        choiceBox.setMinWidth(w);
    }

    @Override
    public void setHeight(double h) {
        this.height = h;
        choiceBox.setMinHeight(h);
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
        choiceBox.setPrefWidth(w);
    }

    @Override
    public void setPrefHeight(double h) {
        this.prefHeight = h;
        choiceBox.setPrefHeight(h);
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
        choiceBox.setMaxWidth(w);
    }

    @Override
    public void setMaxHeight(double h) {
        this.maxHeight = h;
        choiceBox.setMaxHeight(h);
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
