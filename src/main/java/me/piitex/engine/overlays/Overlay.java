package me.piitex.engine.overlays;

import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;
import me.piitex.engine.Container;
import me.piitex.engine.Element;
import me.piitex.engine.Window;
import me.piitex.engine.hanlders.events.*;
import me.piitex.engine.overlays.events.IOverlayClick;
import me.piitex.engine.overlays.events.IOverlayClickRelease;
import me.piitex.engine.overlays.events.IOverlayHover;
import me.piitex.engine.overlays.events.IOverlayHoverExit;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * An overlay is a visual element which can be rendered. The overlay class is the JavaFX equivalent of a {@link Node}.
 * All overlays have generic events that are fired. For example, the {@link OverlayClickEvent} is fired if the overlay is clicked.
 *
 * <p>
 * To render an overlay you first need to add it to a {@link Container}. The container will have to be managed to a {@link Window}.
 * The window is used to render the screen.
 * <pre>
 *     {@code
 *       // Create the overlay
 *       TextOverlay overlay = new TextOverlay("Text");
 *
 *       // Create or fetch the container.
 *       Container container = new EmptyContainer(x, y, width, height, displayOrder);
 *
 *       // Add the overlay to the container.
 *       container.addOverlay(overlay);
 *
 *       // Add the container to the window if needed.
 *       window.addContainer(container);
 *     }
 * </pre>
 *
 * Handling overlay events are key to creation a functional game. During the rendering process, logical programming must be executed with events.
 * <pre>
 *     {@code
 *       // Create the overlay
 *       TextOverlay overlay = new TextOverlay("Text");
 *
 *       // Handle code when the overlay is clicked.
 *       overlay.onClick(event -> {
 *          // Handle logic
 *          System.out.println("The overlay was clicked!");
 *       });
 *     }
 * </pre>
 */
public abstract class Overlay extends Element {
    private double x,y;
    private double scaleX, scaleY;
    private String tooltip;

    private IOverlayHover iOverlayHover;
    private IOverlayHoverExit iOverlayHoverExit;
    private IOverlayClick iOverlayClick;
    private IOverlayClickRelease iOverlayClickRelease;
    private Cursor cursor;
    private Node node;


    // Specific style sheet files
    private final List<File> styleSheets = new ArrayList<>();

    // Style classes.
    private List<String> styles = new LinkedList<>();

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;

        if (node != null) {
            node.setTranslateY(x);
        }
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;

        if (node != null) {
            node.setTranslateY(y);
        }
    }

    public String getTooltip() {
        return tooltip;
    }

    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }

    public double getScaleX() {
        return scaleX;
    }

    public void setScaleX(double scaleX) {
        this.scaleX = scaleX;
    }

    public double getScaleY() {
        return scaleY;
    }

    public void setScaleY(double scaleY) {
        this.scaleY = scaleY;
    }

    public void onClick(IOverlayClick iOverlayClick) {
        this.iOverlayClick = iOverlayClick;
    }

    public void onHover(IOverlayHover iOverlayHover) {
        this.iOverlayHover = iOverlayHover;
    }

    public void onClickRelease(IOverlayClickRelease iOverlayClickRelease) {
        this.iOverlayClickRelease = iOverlayClickRelease;
    }

    public void onHoverExit(IOverlayHoverExit iOverlayHoverExit) {
        this.iOverlayHoverExit = iOverlayHoverExit;
    }

    public IOverlayClick getOnClick() {
        return iOverlayClick;
    }

    public IOverlayHover getOnHover() {
        return iOverlayHover;
    }

    public IOverlayHoverExit getOnHoverExit() {
        return iOverlayHoverExit;
    }

    public IOverlayClickRelease getOnRelease() {
        return iOverlayClickRelease;
    }

    public List<File> getStyleSheets() {
        return styleSheets;
    }

    public void addStyleSheet(File file) {
        this.styleSheets.add(file);
    }

    public void addStyle(String style) {
        styles.add(style);
    }

    public List<String> getStyles() {
        return styles;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public Cursor getCursor() {
        return cursor;
    }

    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
    }

    public boolean isEnabled() {
        if (getNode() != null) {
            return !getNode().isDisable();
        }

        return false;
    }

    public void setEnabled(boolean enabled) {
        if (getNode() != null) {
            getNode().setDisable(!enabled);
        }
    }

    /**
     * Converts the overlay into a {@link Node} which is used for the JavaFX API.
     * @return The converted {@link Node} for the overlay.
     */
    public abstract Node render();

    @Override
    public Node assemble() {
        return render();
    }

    public void setInputControls(Node node) {
        if (cursor != null) {
            node.setCursor(cursor);
        }

        if (tooltip != null && !tooltip.isEmpty()) {
            Tooltip tooltip = new Tooltip(getTooltip());
            tooltip.setAutoHide(false);
            tooltip.setShowDuration(Duration.seconds(30));
            tooltip.setWrapText(true);
            tooltip.setShowDelay(Duration.millis(250));

            node.onMouseExitedProperty().addListener((observable, oldValue, newValue) -> {
                tooltip.hide();
            });

            if (node instanceof Control control) {
                control.setTooltip(tooltip);
            } else {
                Tooltip.install(node, tooltip);
            }
        }

        if (node.getOnDragEntered() == null) {
            node.setOnMouseEntered(event -> {
                //RenJava.getEventHandler().callEvent(new OverlayHoverEvent(this, event));
                if (getOnHover() != null) {
                    getOnHover().onHover(new OverlayHoverEvent(this, event));
                }
            });
        }
        if (node.getOnMouseClicked() == null) {
            node.setOnMouseClicked(event -> {
                OverlayClickEvent overlayClickEvent = new OverlayClickEvent(this, event, event.getSceneX(), event.getSceneY());
                if (getOnClick() != null) {
                    getOnClick().onClick(overlayClickEvent);
                }
            });
        }
        if (node.getOnMouseExited() == null) {
            node.setOnMouseExited(event -> {
                if (getOnHoverExit() != null) {
                    getOnHoverExit().onHoverExit(new OverlayExitEvent(this, event));
                }
            });
        }
        if (node.getOnMouseReleased() == null) {
            node.setOnMouseReleased(event -> {
                if (getOnRelease() != null) {
                    getOnRelease().onClickRelease(new OverlayClickReleaseEvent(this, event));
                }
            });
        }

        if (node instanceof TextArea textArea) {
            if (node.getOnKeyPressed() == null) {
                node.setOnKeyPressed(event -> {
                    if (event.isShiftDown() && event.getCode() == KeyCode.ENTER) {
                        textArea.appendText("\n");
                    } else if (event.getCode() == KeyCode.ENTER) {
                        //TODO: Handle submit action for the overlay.
                    }
                });
            }
        }
    }
}