package me.piitex.engine.containers;

import atlantafx.base.controls.Card;
import javafx.scene.Node;
import me.piitex.engine.Container;
import me.piitex.engine.Element;
import me.piitex.engine.hanlders.events.ContainerClickEvent;
import me.piitex.engine.hanlders.events.LayoutRenderEvent;
import me.piitex.engine.layouts.Layout;
import me.piitex.engine.overlays.Overlay;

import java.util.AbstractMap;
import java.util.LinkedList;
import java.util.Map;
public class CardContainer extends Container {

    private final Card atlantafxCard;

    private Overlay headerOverlay;
    private Layout headerLayout;
    private Overlay subHeaderOverlay;
    private Layout subHeaderLayout;
    private Overlay bodyOverlay;
    private Layout bodyLayout;
    private Overlay footerOverlay;
    private Layout footerLayout;

    public CardContainer(double x, double y, double width, double height) {
        super(x, y, width, height);
        this.atlantafxCard = new Card();
    }

    public CardContainer(double width, double height) {
        super(0, 0, width, height);
        this.atlantafxCard = new Card();
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

    public CardContainer setHeader(Overlay overlay) {
        this.headerOverlay = overlay;
        this.headerLayout = null; // Clear layout if overlay is set
        return this;
    }

    public CardContainer setHeader(Layout layout) {
        this.headerLayout = layout;
        this.headerOverlay = null; // Clear overlay if layout is set
        return this;
    }

    public CardContainer setSubHeader(Overlay overlay) {
        this.subHeaderOverlay = overlay;
        this.subHeaderLayout = null; // Clear layout if overlay is set
        return this;
    }

    public CardContainer setSubHeader(Layout layout) {
        this.subHeaderLayout = layout;
        this.subHeaderOverlay = null; // Clear overlay if layout is set
        return this;
    }

    public CardContainer setBody(Overlay overlay) {
        this.bodyOverlay = overlay;
        this.bodyLayout = null;
        return this;
    }

    public CardContainer setBody(Layout layout) {
        this.bodyLayout = layout;
        this.bodyOverlay = null;
        return this;
    }

    public CardContainer setFooter(Overlay overlay) {
        this.footerOverlay = overlay;
        this.footerLayout = null;

        return this;
    }

    public CardContainer setFooter(Layout layout) {
        this.footerLayout = layout;
        this.footerOverlay = null;

        return this;
    }

    @Override
    public Map.Entry<Node, LinkedList<Node>> build() {
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

        if (headerOverlay != null) {
            Node node = headerOverlay.render();
            headerOverlay.setInputControls(node);
            headerOverlay.setNode(node);
            atlantafxCard.setHeader(node);
            if (headerOverlay.getId() != null && !headerOverlay.getId().isEmpty()) {
                getRenderedNodes().put(headerOverlay.getId(), node);
            }
        } else if (headerLayout != null) {
            Node node = headerLayout.render();
            if (headerLayout.getRenderEvent() != null) {
                headerLayout.getRenderEvent().onLayoutRender(new LayoutRenderEvent(headerLayout.getPane(), headerLayout));
            }
            atlantafxCard.setHeader(node);
        }

        if (subHeaderOverlay != null) {
            atlantafxCard.setSubHeader(subHeaderOverlay.render());
        } else if (subHeaderLayout != null) {
            Node node = subHeaderLayout.render();
            if (subHeaderLayout.getRenderEvent() != null) {
                subHeaderLayout.getRenderEvent().onLayoutRender(new LayoutRenderEvent(subHeaderLayout.getPane(), subHeaderLayout));
            }
            atlantafxCard.setSubHeader(node);
        }

        if (bodyOverlay != null) {
            Node node = bodyOverlay.render();
            bodyOverlay.setInputControls(node);
            bodyOverlay.setNode(node);
            atlantafxCard.setBody(node);
            if (bodyOverlay.getId() != null && !bodyOverlay.getId().isEmpty()) {
                getRenderedNodes().put(bodyOverlay.getId(), node);
            }
        } else if (bodyLayout != null) {
            Node node = bodyLayout.render();
            if (bodyLayout.getRenderEvent() != null) {
                bodyLayout.getRenderEvent().onLayoutRender(new LayoutRenderEvent(bodyLayout.getPane(), bodyLayout));
            }
            atlantafxCard.setBody(node);
        }

        if (footerOverlay != null) {
            Node node = footerOverlay.render();
            footerOverlay.setInputControls(node);
            footerOverlay.setNode(node);
            atlantafxCard.setFooter(node);
            if (footerOverlay.getId() != null && !footerOverlay.getId().isEmpty()) {
                getRenderedNodes().put(footerOverlay.getId(), node);
            }
        } else if (footerLayout != null) {
            Node node = footerLayout.render();
            if (footerLayout.getRenderEvent() != null) {
                footerLayout.getRenderEvent().onLayoutRender(new LayoutRenderEvent(bodyLayout.getPane(), footerLayout));
            }
            atlantafxCard.setFooter(node);
        }

        if (getOnClick() != null) {
            atlantafxCard.setOnMouseClicked(mouseEvent -> {
                getOnClick().onClick(new ContainerClickEvent(this));
            });
        }

        atlantafxCard.getStyleClass().addAll(getStyles());


        return new AbstractMap.SimpleEntry<>(atlantafxCard, new LinkedList<>());
    }
}