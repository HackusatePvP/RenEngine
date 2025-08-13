package me.piitex.engine.containers.tabs;

import javafx.scene.Node;
import javafx.scene.control.TabPane;
import me.piitex.engine.Container;
import me.piitex.engine.Element;

import java.util.AbstractMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

public class TabsContainer extends Container {
    private final TabPane tabPane;
    private final Map<String, Tab> tabs = new LinkedHashMap<>();
    private boolean closeTabs = false;
    private String selectedTab;

    public TabsContainer(double x, double y, double width, double height) {
        super(new TabPane(), x, y, width, height);
        this.tabPane = (TabPane) getView();
    }

    public TabsContainer(double x, double y, double width, double height, int index) {
        super(new TabPane(), x, y, width, height, index);
        this.tabPane = (TabPane) getView();
    }

    public Map<String, Tab> getTabs() {
        return tabs;
    }

    public void addTab(Tab tab) {
        this.tabs.put(tab.getText(), tab);
        tabPane.getTabs().add(tab.render());
    }

    public void replaceTab(Tab oldTab, Tab newTab) {
        this.tabs.replace(oldTab.getText(), newTab);
        int index = tabPane.getTabs().indexOf(oldTab.getJfxTab());
        if (index != -1) {
            tabPane.getTabs().set(index, newTab.render());
        }
    }

    public void setCloseTabs(boolean closeTabs) {
        this.closeTabs = closeTabs;
    }

    public String getSelectedTab() {
        return selectedTab;
    }

    public void setSelectedTab(String selectedTab) {
        this.selectedTab = selectedTab;
        tabPane.getSelectionModel().select(tabs.get(selectedTab).getJfxTab());
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
    public Node build() {
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

        tabPane.setTranslateX(getX());
        tabPane.setTranslateY(getY());
        tabPane.setPrefSize(getWidth(), getHeight());

        
        return tabPane;
    }
}
