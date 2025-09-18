package me.piitex.engine.containers;

import atlantafx.base.controls.Card;
import javafx.scene.Node;
import me.piitex.engine.Container;
import me.piitex.engine.Element;
import me.piitex.engine.hanlders.events.ContainerClickEvent;

import java.util.LinkedList;
public class CardContainer extends Container {

    private final Card atlantafxCard;

    private Element header;
    private Element subHeader;
    private Element body;
    private Element footer;

    public CardContainer(double x, double y, double width, double height) {
        // Java 25 Update: You can now call code before the super() method
        // I think this is slightly better than confusing casting.
        Card card = new Card();
        this.atlantafxCard = card;
        super(card, x, y, width, height);
    }

    public CardContainer(double width, double height) {
        Card card = new Card();
        this.atlantafxCard = card;
        super(card, 0, 0, width, height);
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

    public void setHeader(Element element) {
        this.header = element;
        atlantafxCard.setHeader(element.assemble());
    }

    public void setSubHeader(Element element) {
        this.subHeader = element;
        atlantafxCard.setSubHeader(element.assemble());
    }

    public void setBody(Element element) {
        this.body = element;
        atlantafxCard.setBody(element.assemble());
    }

    public void setFooter(Element element) {
        this.footer = element;
        atlantafxCard.setFooter(element.assemble());
    }

    public Element getHeader() {
        return header;
    }

    public Element getSubHeader() {
        return subHeader;
    }

    public Element getBody() {
        return body;
    }

    public Element getFooter() {
        return footer;
    }

    public Card getCard() {
        return atlantafxCard;
    }

    @Override
    public Node build() {
        // Ensure the card's position and size match the container's
        atlantafxCard.setTranslateX(getX());
        atlantafxCard.setTranslateY(getY());

        if (getWidth() > 0) {
            atlantafxCard.setMinWidth(getWidth());
        }
        if (getHeight() > 0) {
            atlantafxCard.setMinHeight(getHeight());
        }

        if (getPrefWidth() > 0) {
            atlantafxCard.setPrefWidth(getPrefWidth());
        }
        if (getPrefHeight() > 0) {
            atlantafxCard.setPrefHeight(getPrefHeight());
        }

        if (getMaxWidth() > 0) {
            atlantafxCard.setMaxWidth(getMaxWidth());
        }
        if (getMaxHeight() > 0) {
            atlantafxCard.setMaxHeight(getMaxHeight());
        }

        if (getOnClick() != null) {
            atlantafxCard.setOnMouseClicked(mouseEvent -> {
                getOnClick().onClick(new ContainerClickEvent(this));
            });
        }

        atlantafxCard.getStyleClass().addAll(getStyles());


        return atlantafxCard;
    }
}