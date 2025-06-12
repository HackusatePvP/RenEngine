package me.piitex.engine.containers.tabs;

import javafx.scene.Node;
import javafx.scene.control.TabPane;
import me.piitex.engine.Container;
import me.piitex.engine.Element;

import java.util.AbstractMap;
import java.util.LinkedList;
import java.util.Map;

public class TabsContainer extends Container {
    private final LinkedList<Tab> tabs = new LinkedList<>();
    private boolean closeTabs = false;

    public TabsContainer(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    public TabsContainer(double x, double y, double width, double height, int index) {
        super(x, y, width, height, index);
    }

    public LinkedList<Tab> getTabs() {
        return tabs;
    }

    public void addTab(Tab tab) {
        this.tabs.add(tab);
    }

    public void setCloseTabs(boolean closeTabs) {
        this.closeTabs = closeTabs;
    }

    /**
     * Method does not work with this container.
     */
    @Override
    public void addElement(Element element) {
        return;
    }

    /**
     * Method does not work with this container.
     */
    @Override
    public void addElements(Element... elements) {
        return;
    }

    /**
     * Method does not work with this container.
     */
    @Override
    public void addElements(LinkedList<Element> elements) {
        return;
    }

    @Override
    public Map.Entry<Node, LinkedList<Node>> build() {
        TabPane pane = new TabPane();
        if (closeTabs) {
            pane.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);
        } else {
            pane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        }

        for (Tab tab : tabs) {
            pane.getTabs().add(tab.render());
        }

        pane.setTranslateX(getX());
        pane.setTranslateY(getY());
        pane.setPrefSize(getWidth(), getHeight());
        System.out.println("Tab Size: " + tabs.size());
        LinkedList<Node> order = buildBase();

        return new AbstractMap.SimpleEntry<>(pane, order);
    }
}
