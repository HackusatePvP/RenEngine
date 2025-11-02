package me.piitex.engine.containers;

import javafx.scene.Node;
import javafx.scene.control.TreeTableView;
import javafx.util.Callback;
import me.piitex.engine.Container;
import me.piitex.engine.containers.tree.TreeTab;

import java.util.*;

public class TreeTableContainer<S> extends Container {
    private final TreeMap<Integer, TreeTab<S, ?>> tabs = new TreeMap<>();
    private Callback<TreeTableView.ResizeFeatures, Boolean> resizePolicy = null;

    public TreeTableContainer() {
        super(null, 0, 0, 0, 0, 0);
    }

    public TreeTableContainer(double width, double height) {
        super(null, 0, 0, width, height);
    }

    public TreeTableContainer(double x, double y, double width, double height) {
        super(null, x, y, width, height);
    }

    public TreeTableContainer(double x, double y, double width, double height, int index) {
        super(null, x, y, width, height, index);
    }

    public void setResizePolicy(Callback<TreeTableView.ResizeFeatures, Boolean> resizePolicy) {
        this.resizePolicy = resizePolicy;
    }

    @Override
    public Node build() {
        TreeTableView<S> tableView = new TreeTableView<>();
        tableView.setTranslateX(getX());
        tableView.setTranslateY(getY());
        if (getWidth() > 0) {
            tableView.setMinWidth(getWidth());
        }
        if (getHeight() > 0) {
            tableView.setMinHeight(getHeight());
        }
        if (getPrefWidth() > 0) {
            tableView.setPrefWidth(getPrefWidth());
        }
        if (getPrefHeight() > 0) {
            tableView.setPrefHeight(getPrefHeight());
        }
        if (getMaxWidth() > 0) {
            tableView.setMaxWidth(getMaxWidth());
        }
        if (getMaxHeight() > 0) {
            tableView.setMaxHeight(getMaxHeight());
        }
        if (resizePolicy != null) {
            tableView.setColumnResizePolicy(resizePolicy);
        }

        tabs.forEach((integer, treeTab) -> {
            tableView.getColumns().add(treeTab.render());
        });

        tableView.getStyleClass().addAll(getStyles());

        return tableView;
    }
}
