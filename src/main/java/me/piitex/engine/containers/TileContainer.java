package me.piitex.engine.containers;

import atlantafx.base.controls.Tile;
import javafx.scene.Node;

import me.piitex.engine.Element;

import java.util.LinkedList;

public class TileContainer extends Container {
    private final Tile tile;
    private Element graphic;
    private String title;
    private String description;
    private Element action;

    public TileContainer(double x, double y, double width, double height) {
        Tile tempTile = new Tile();
        this.tile = tempTile;
        super(tempTile, x, y, width, height);
    }

    public TileContainer() {
        this(0,0,0,0);
    }

    public TileContainer(double width, double height) {
        this(0, 0, width, height);
    }

    public Element getGraphic() {
        return graphic;
    }

    public void setGraphic(Element graphic) {
        this.graphic = graphic;
        tile.setGraphic(graphic.assemble());
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        tile.setTitle(title);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        tile.setDescription(description);
    }

    public void setAction(Element action) {
        this.action = action;
        tile.setAction(action.assemble());
    }

    public Element getAction() {
        return action;
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

    public Tile getTile() {
        return tile;
    }

    @Override
    public Node build() {
        tile.setTitle(title);
        tile.setDescription(description);
        if (graphic != null && tile.getGraphic() != null) {
            Node node = graphic.assemble();
            tile.setGraphic(node);
        }
        if (action != null && tile.getAction() == null) {
            Node node = action.assemble();
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

        return tile;
    }
}
