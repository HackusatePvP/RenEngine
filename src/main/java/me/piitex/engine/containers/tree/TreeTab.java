package me.piitex.engine.containers.tree;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.TreeTableColumn;
import javafx.util.Callback;
import me.piitex.engine.Renderer;

public class TreeTab<S,T> extends Renderer {
    private final TreeTableColumn<S, T> column;

    public TreeTab() {
        this.column = new TreeTableColumn<>();
    }

    public TreeTableColumn<S, T> render() {
        if (getWidth() > 0) {
            column.setMinWidth(getWidth());
        }
        if (getPrefWidth() > 0) {
            column.setPrefWidth(getPrefWidth());
        }
        if (getMaxWidth() > 0) {
            column.setMaxWidth(getMaxWidth());
        }
        column.getStyleClass().addAll(getStyles());

        return column;
    }

    public void setCellValueFactory(Callback<TreeTableColumn.CellDataFeatures<S,T>, ObservableValue<T>> value) {
        column.setCellValueFactory(value);
    }

}
