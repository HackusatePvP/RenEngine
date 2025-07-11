package me.piitex.engine.containers;

import atlantafx.base.controls.Tile;
import javafx.scene.Node;

import me.piitex.engine.Container;
import me.piitex.engine.Element;
import me.piitex.engine.overlays.Overlay;

import java.util.AbstractMap;
import java.util.LinkedList;
import java.util.Map;

public class TileContainer extends Container {
    private Overlay graphic;
    private String title;
    private String description;
    private Overlay action;

    public TileContainer() {
        super(0, 0, 0, 0);
    }

    public TileContainer(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    public TileContainer(double width, double height) {
        super(0, 0, width, height);
    }

    public Overlay getGraphic() {
        return graphic;
    }

    public void setGraphic(Overlay graphic) {
        this.graphic = graphic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAction(Overlay action) {
        this.action = action;
    }

    @Override
    public void addElements(Element... elements) {
        return;
    }

    @Override
    public void addElements(LinkedList<Element> elements) {
        return;
    }

    @Override
    public void addElement(Element element) {
        return;
    }

    @Override
    public void addElement(Element element, int index) {
        return;
    }

    @Override
    public Map.Entry<Node, LinkedList<Node>> build() {
        Tile tile = new Tile();
        tile.setTitle(title);
        tile.setDescription(description);
        if (graphic != null) {
            Node node = graphic.render();
            graphic.setInputControls(node);
            graphic.setNode(node);
            tile.setGraphic(node);
        }
        if (action != null) {
            Node node = action.render();
            action.setInputControls(node);
            action.setNode(node);
            tile.setAction(node);
        }
        tile.setTranslateX(getX());
        tile.setTranslateY(getY());

        if (getWidth() > 0) {
            tile.setMinWidth(getWidth());
        }
        if (getPrefWidth() > 0) {
            tile.setPrefWidth(getPrefWidth());
        }
        if (getMaxWidth() > 0) {
            tile.setMaxWidth(getMaxWidth());
        }

        if (getHeight() > 0) {
            tile.setMinHeight(getHeight());
        }

        if (getPrefHeight() > 0) {
            tile.setPrefHeight(getPrefHeight());
        }

        if (getMaxHeight() > 0) {
            tile.setMaxHeight(getMaxHeight());
        }

        setStyling(tile);

        return new AbstractMap.SimpleEntry<>(tile, new LinkedList<>());
    }
}
