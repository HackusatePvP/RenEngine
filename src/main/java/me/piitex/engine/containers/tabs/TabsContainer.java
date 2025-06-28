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
    private String selectedTab;
    private TabPane tabPane;

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

    public String getSelectedTab() {
        return selectedTab;
    }

    public void setSelectedTab(String selectedTab) {
        this.selectedTab = selectedTab;
    }

    public TabPane getTabPane() {
        return tabPane;
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
        tabPane = new TabPane();
        if (getWidth() > 0) {
            tabPane.setMinWidth(getWidth());
        }
        if (getHeight() > 0) {
            tabPane.setMinHeight(getHeight());
        }

        if (getPrefWidth() > 0) {
            tabPane.setPrefWidth(getPrefWidth());
        }
        if (getPrefHeight() > 0) {
            tabPane.setPrefHeight(getPrefHeight());
        }

        if (getMaxWidth() > 0) {
            tabPane.setMaxWidth(getMaxWidth());
        }
        if (getMaxHeight() > 0) {
            tabPane.setMaxHeight(getMaxHeight());
        }
        if (closeTabs) {
            tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);
        } else {
            tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        }

        for (Tab tab : tabs) {
            javafx.scene.control.Tab t = tab.render();
            tabPane.getTabs().add(t);
            if (tab.getText().equalsIgnoreCase(selectedTab)) {
                tabPane.getSelectionModel().select(t);
            }
        }

        tabPane.setTranslateX(getX());
        tabPane.setTranslateY(getY());
        tabPane.setPrefSize(getWidth(), getHeight());

        LinkedList<Node> order = buildBase();

        return new AbstractMap.SimpleEntry<>(tabPane, order);
    }
}
