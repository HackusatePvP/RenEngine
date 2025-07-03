package me.piitex.engine.containers;

import atlantafx.base.controls.Card;
import atlantafx.base.controls.Tile;
import atlantafx.base.theme.Styles;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import me.piitex.engine.Container;
import me.piitex.engine.Element;
import me.piitex.engine.overlays.ButtonOverlay;
import me.piitex.engine.overlays.Overlay;

import java.util.AbstractMap;
import java.util.LinkedList;
import java.util.Map;

public class DialogueContainer extends Container {
    private String header;
    private String body;
    private ButtonOverlay confirmButton;
    private ButtonOverlay cancelButton;

    public DialogueContainer(String header, double width, double height) {
        super(0, 0, width, height);
        this.header = header;
    }

    public DialogueContainer(String header, double x, double y, double width, double height) {
        super(0, 0, width, height);
        this.header = header;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public ButtonOverlay getConfirmButton() {
        return confirmButton;
    }

    public void setConfirmButton(ButtonOverlay confirmButton) {
        this.confirmButton = confirmButton;
    }

    public ButtonOverlay getCancelButton() {
        return cancelButton;
    }

    public void setCancelButton(ButtonOverlay cancelButton) {
        this.cancelButton = cancelButton;
    }

    /**
     * Method not supported with this container.
     */
    @Override
    public void addElement(Element element) {
        return;
    }

    /**
     * Method not supported with this container.
     */
    @Override
    public void addElement(Element element, int index) {
        return;
    }

    /**
     * Method not supported with this container.
     */
    @Override
    public void addElements(Element... elements) {
        return;
    }

    /**
     * Method not supported with this container.
     */
    @Override
    public void addElements(LinkedList<Element> elements) {
        return;
    }


    @Override
    public Map.Entry<Node, LinkedList<Node>> build() {
        Pane pane = new Pane();
        pane.setTranslateX(getX());
        pane.setTranslateY(getY());

        pane.getStyleClass().add(Styles.ELEVATED_1);

        if (getWidth() > 0) {
            pane.setMinWidth(getWidth());
        }
        if (getHeight() > 0) {
            pane.setMinHeight(getHeight());
        }

        if (getPrefWidth() > 0) {
            pane.setPrefWidth(getPrefWidth());
        }
        if (getPrefHeight() > 0) {
            pane.setPrefHeight(getPrefHeight());
        }

        if (getMaxWidth() > 0) {
            pane.setMaxWidth(getMaxWidth());
        }
        if (getMaxHeight() > 0) {
            pane.setMaxHeight(getMaxHeight());
        }

        Card card = new Card();
        card.setHeader(new Tile(header, body));

        HBox buttonBox = new HBox(20, cancelButton.render(), confirmButton.render());
        card.setFooter(buttonBox);

        LinkedList<Node> order = new LinkedList<>();
        order.add(card);

        return new AbstractMap.SimpleEntry<>(pane, order);
    }

}
