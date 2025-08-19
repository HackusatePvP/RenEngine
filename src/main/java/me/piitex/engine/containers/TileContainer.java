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
    private final Tile tile;
    private Overlay graphic;
    private String title;
    private String description;
    private Overlay action;

    public TileContainer() {
        super(new Tile(), 0, 0, 0, 0);
        this.tile = (Tile) getView();
    }

    public TileContainer(double x, double y, double width, double height) {
        super(new Tile(), x, y, width, height);
        this.tile = (Tile) getView();
    }

    public TileContainer(double width, double height) {
        super(new Tile(), 0, 0, width, height);
        this.tile = (Tile) getView();
    }

    public Overlay getGraphic() {
        return graphic;
    }

    public void setGraphic(Overlay graphic) {
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

    public void setAction(Overlay action) {
        this.action = action;
        tile.setAction(action.assemble());
    }

    public Overlay getAction() {
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

    @Override
    public Node build() {
        tile.setTitle(title);
        tile.setDescription(description);
        if (graphic != null && tile.getGraphic() != null) {
            Node node = graphic.assemble();
            graphic.setInputControls(node);
            graphic.setNode(node);
            tile.setGraphic(node);
        }
        if (action != null && tile.getAction() == null) {
            Node node = action.assemble();
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

        return tile;
    }
}
