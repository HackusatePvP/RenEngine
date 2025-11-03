package me.piitex.engine.containers;

import javafx.scene.Node;
import javafx.scene.control.Pagination;
import me.piitex.engine.Element;
import me.piitex.engine.Renderer;

import java.util.LinkedList;
import java.util.List;

public class PaginationContainer extends Container {
    private final Pagination pagination;
    private final List<Renderer> pages = new LinkedList<>();

    public PaginationContainer(double x, double y, double width, double height) {
        Pagination tempPage = new Pagination();
        this.pagination = tempPage;
        super(tempPage, x, y, width, height);
    }

    public PaginationContainer(double width, double height) {
        this(0, 0, width, height);
    }

    /**
     * Adds a page (which can be a Layout or Container) to the pagination.
     *
     * @param page The Renderer element representing a page.
     */
    public void addPage(Renderer page) {
        if (page != null) {
            pages.add(page);
            pagination.setPageCount(pages.size());
        }
    }

    public List<Renderer> getPages() {
        return pages;
    }

    @Override
    public Node build() {
        // Configure the Pagination control's page factory
        pagination.setPageFactory(pageIndex -> {
            if (pageIndex < 0 || pageIndex >= pages.size()) {
                return null;
            }
            Renderer pageContent = pages.get(pageIndex);

            // Assembles the content of the selected page.
            // Your existing assemble() method handles the conversion to a JavaFX Node.
            return pageContent.assemble();
        });

        // Apply any styling or positioning from the base Container class
        pagination.setTranslateX(getX());
        pagination.setTranslateY(getY());

        if (getWidth() > 0) {
            pagination.setMinWidth(getWidth());
        }
        if (getHeight() > 0) {
            pagination.setMinHeight(getHeight());
        }

        // Return the fully configured Pagination control
        return pagination;
    }

    /**
     * Overridden to prevent adding elements directly to the PaginationContainer,
     * as elements should be added to the individual pages instead.
     */
    @Override
    public void addElement(Element element) {
        return;
    }
}