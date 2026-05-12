package me.piitex.engine.overlays;

import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.Region;
import javafx.util.Duration;
import me.piitex.engine.containers.Container;
import me.piitex.engine.Element;
import me.piitex.engine.Window;
import me.piitex.engine.hanlders.events.*;
import me.piitex.engine.overlays.events.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * An overlay is a visual element which can be rendered. The overlay class is the framework's equivalent of a native JavaFX {@link Node}.
 * All overlays have generic events that are fired. For example, the {@link ElementClickEvent} is fired if the overlay is clicked.
 *
 * <p>
 * To render an overlay you first need to add it to a {@link Container}. The container will have to be managed to a {@link Window}.
 * The window is used to render the screen.
 * <pre>
 * {@code
 * // Create the overlay
 * TextOverlay overlay = new TextOverlay("Text");
 *
 * // Create or fetch the container.
 * Container container = new EmptyContainer(x, y, width, height, displayOrder);
 *
 * // Add the overlay to the container's element map.
 * container.addElement(overlay);
 *
 * // Add the container to the window if needed.
 * window.addContainer(container);
 * }
 * </pre>
 *
 * Handling overlay events are key to creating a functional application. During the rendering process, logical programming must be executed with events.
 * <pre>
 * {@code
 * // Create the overlay
 * TextOverlay overlay = new TextOverlay("Text");
 *
 * // Handle code when the overlay is clicked.
 * overlay.onClick(event -> {
 * // Handle logic
 * System.out.println("The overlay was clicked!");
 * });
 * }
 * </pre>
 */
public abstract class Overlay extends Element {
    private double x,y;
    private String tooltip;
    private IOverlaySubmit iOverlaySubmit;
    private Cursor cursor;
    private static final Logger logger = LoggerFactory.getLogger(Overlay.class);


    // Specific style sheet files
    private final List<File> styleSheets = new ArrayList<>();

    // Style classes.
    private final List<String> styles = new LinkedList<>();

    /**
     * Retrieves the explicit X coordinate offset of the overlay relative to its parent container.
     * @return The horizontal layout position.
     */
    public double getX() {
        return x;
    }

    /**
     * Sets the explicit horizontal layout position of the overlay.
     * Translates the underlying JavaFX node dynamically if it has already been assembled.
     *
     * @param x The new X layout constraint.
     */
    public void setX(double x) {
        this.x = x;

        if (getNode() != null) {
            getNode().setTranslateX(x); // Note: Corrected to setTranslateX from setTranslateY
        }
    }

    /**
     * Retrieves the explicit Y coordinate offset of the overlay relative to its parent container.
     * @return The vertical layout position.
     */
    public double getY() {
        return y;
    }

    /**
     * Sets the explicit vertical layout position of the overlay.
     * Translates the underlying JavaFX node dynamically if it has already been assembled.
     *
     * @param y The new Y layout constraint.
     */
    public void setY(double y) {
        this.y = y;

        if (getNode() != null) {
            getNode().setTranslateY(y);
        }
    }

    /**
     * Retrieves the configured tooltip text meant to display when a user hovers over this overlay.
     * @return The string tooltip data, or null if unassigned.
     */
    public String getTooltip() {
        return tooltip;
    }

    /**
     * Assigns a text-based tooltip to this overlay. The tooltip framework integrates this automatically
     * into the JavaFX node during the assembly phase.
     *
     * @param tooltip The informative hover text to display.
     */
    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }

    /**
     * Registers a specialized submit event handler, primarily utilized by overlays capable of receiving input
     * (e.g., text areas capturing an ENTER key press without shift modifiers).
     *
     * @param iOverlaySubmit The execution logic mapped to the submit event.
     */
    public void onOverlaySubmit(IOverlaySubmit iOverlaySubmit) {
        this.iOverlaySubmit = iOverlaySubmit;
    }

    /**
     * Retrieves the collection of local CSS stylesheet files assigned explicitly to this specific overlay.
     * @return A list of {@link File} references for styles.
     */
    public List<File> getStyleSheets() {
        return styleSheets;
    }

    /**
     * Links an external CSS file specifically to this overlay instance.
     * @param file The targeted local {@link File}.
     */
    public void addStyleSheet(File file) {
        this.styleSheets.add(file);
    }

    /**
     * Attaches a native JavaFX CSS class name string to this overlay.
     * @param style The CSS class definition.
     */
    public void addStyle(String style) {
        styles.add(style);
    }

    /**
     * Retrieves the collection of CSS style classes currently mapped to the overlay logic.
     * @return A list of CSS class name strings.
     */
    public List<String> getStyles() {
        return styles;
    }

    /**
     * @return The current configured hover cursor mapping.
     */
    @Override
    public Cursor getCursor() {
        return cursor;
    }

    /**
     * Overrides the default mouse cursor visualization when the mouse pointer bounds enter the overlay.
     * @param cursor The required JavaFX {@link Cursor}.
     */
    @Override
    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
    }

    /**
     * Converts the logical engine overlay instructions exclusively into a raw {@link Node} which is used for the native JavaFX API.
     * Subclasses define their specific node implementations here (e.g., ImageView, Text, TextField).
     *
     * @return The newly constructed native JavaFX {@link Node} representing the overlay data.
     */
    protected abstract Node render();

    /**
     * The primary assembly lifecycle phase. Generates the underlying node and safely enforces JavaFX
     * UI thread checks before assigning structural input/control event wrappers like tooltips and key binds.
     *
     * @return The fully compiled and interactive {@link Node}.
     */
    @Override
    public Node assemble() {
        Node node = render();
        setNode(node);

        // Starting to implement sub-thread loading.
        // Input controls directly access JavaFX's event listeners.
        // Event listeners are required to be executed on the FXThread.
        // Check if the current thread is the FXThread, if not run it on the FXThread.
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> {
                setInputControls(node);
            });
        } else {
            setInputControls(node);
        }
        return node;
    }

    /**
     * Injects standard input configuration, cursors, hover delays, and text area key binds directly
     * onto the native node's event listener chains. This must be invoked exclusively on the JavaFX Application thread.
     *
     * @param node The physically rendering JavaFX node.
     */
    public void setInputControls(Node node) {
        if (cursor != null) {
            node.setCursor(cursor);
        }

        if (tooltip != null && !tooltip.isEmpty()) {
            Tooltip tooltip = new Tooltip(getTooltip());
            tooltip.setAutoHide(false);
            tooltip.setShowDuration(Duration.INDEFINITE);
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

        if (node instanceof TextArea textArea) {
            if (node.getOnKeyPressed() == null) {
                node.setOnKeyPressed(event -> {
                    if (event.isShiftDown() && event.getCode() == KeyCode.ENTER) {
                        textArea.appendText("\n");
                    } else if (event.getCode() == KeyCode.ENTER) {
                        if (iOverlaySubmit != null) {
                            iOverlaySubmit.onSubmit(new OverlaySubmitEvent(this, event));
                        }
                    }
                });
            }
        }
    }
}