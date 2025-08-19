package me.piitex.engine.overlays;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import me.piitex.engine.loaders.FontLoader;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.LinkedList;

public class ButtonOverlay extends Overlay implements Region {
    private final Button button;
    private final String id;
    private String text;
    private FontLoader font;
    private final FontIcon icon;
    private final LinkedList<ImageOverlay> images = new LinkedList<>();
    private Paint textFill;

    private double width, height, prefHeight, prefWidth, maxWidth, maxHeight;
    private double scaleWidth, scaleHeight;

    /**
     * Private constructor used by the {@link ButtonBuilder}.
     * @param builder The builder instance with all the properties set.
     */
    protected ButtonOverlay(ButtonBuilder builder) {
        this.id = builder.getId();
        this.text = builder.getText();
        this.icon = builder.getIcon();
        this.font = builder.getFont();
        this.textFill = builder.getTextFill();
        this.width = builder.getWidth();
        this.height = builder.getHeight();
        this.prefHeight = builder.getPrefHeight();
        this.prefWidth = builder.getPrefWidth();
        this.maxWidth = builder.getMaxWidth();
        this.maxHeight = builder.getMaxHeight();
        this.scaleWidth = builder.getScaleWidth();
        this.scaleHeight = builder.getScaleHeight();
        this.images.addAll(builder.getImages());
        setX(builder.getX());
        setY(builder.getY());
        this.button = new Button();
        button.setDisable(!builder.isEnabled());
    }

    public Button getButton() {
        return button;
    }

    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        button.setText(text);
    }

    public FontLoader getFontLoader() {
        return font;
    }

    public void setFont(FontLoader font) {
        this.font = font;
        button.setFont(font.getFont());
    }

    @Override
    public Node render() {
        button.setId(id);
        button.getStyleClass().addAll(getStyles());
        for (ImageOverlay image : images) {
            if (image != null) {
                ImageView imageView = new ImageView(image.getImage());
                imageView.setFitWidth(width);
                imageView.setFitHeight(height);
                if (image.getX() > 0) {
                    imageView.setX(image.getX());
                }
                if (image.getY() > 0) {
                    imageView.setY(image.getY());
                }
                button.setAlignment(Pos.TOP_CENTER);
                button.setGraphic(imageView);
                if (image.getX() > 0) {
                    button.getGraphic().setTranslateX(image.getX());
                }
                if (image.getY() > 0) {
                    // This can move the image within the button box
                    // The y and x values are separate from the main menu
                    // Top left of the box is 0,0
                    // Bottom right of the box is boxWidth, boxHeight
                    button.getGraphic().setTranslateY(image.getY());
                }
            }
        }
        if (icon != null) {
            button.setGraphic(icon);
        }
        if (text != null && !text.isEmpty()) {
            button.setText(text);
        }
        if (font != null) {
            button.setFont(font.getFont());
        }
        if (textFill != null) {
            button.setTextFill(textFill);
        }
        if (height > 0) {
            button.setMaxHeight(height);
            button.setPrefHeight(height);
        }
        if (width > 0) {
            button.setMinWidth(width);
            button.setMaxWidth(width);
            button.setPrefWidth(width);
        }

        if (getX() != 0 && getY() != 0) {
            button.setTranslateX(getX());
            button.setTranslateY(getY());
        }

        setInputControls(button);
        button.setTranslateX(getX());
        button.setTranslateY(getY());
        return button;
    }

    @Override
    public double getWidth() {
        return width;
    }

    @Override
    public double getHeight() {
        return height;
    }

    @Override
    public void setWidth(double w) {
        this.width = w;
        button.setMinWidth(w);
    }

    @Override
    public void setHeight(double h) {
        this.height = h;
        button.setMinHeight(h);
    }

    @Override
    public double getPrefWidth() {
        return prefWidth;
    }

    @Override
    public double getPrefHeight() {
        return prefHeight;
    }

    @Override
    public void setPrefWidth(double w) {
        this.prefWidth = w;
        button.setPrefWidth(w);
    }

    @Override
    public void setPrefHeight(double h) {
        this.prefHeight = h;
        button.setPrefHeight(h);
    }

    @Override
    public double getMaxWidth() {
        return maxWidth;
    }

    @Override
    public double getMaxHeight() {
        return maxHeight;
    }

    @Override
    public void setMaxWidth(double w) {
        this.maxWidth = w;
        button.setMaxWidth(w);
    }

    @Override
    public void setMaxHeight(double h) {
        this.maxHeight = h;
        button.setMaxHeight(h);
    }

    public LinkedList<ImageOverlay> getImages() {
        return images;
    }

    public void addImage(ImageOverlay imageOverlay) {
        this.images.add(imageOverlay);
        button.setGraphic(imageOverlay.assemble());

    }

    public Paint getTextFill() {
        return textFill;
    }

    public void setTextFill(Paint color) {
        this.textFill = color;
        button.setTextFill(color);
    }

    // Helper css function
    private String cssColor(Color color) {
        return String.format("rgba(%d, %d, %d, %f)",
                (int) (255 * color.getRed()),
                (int) (255 * color.getGreen()),
                (int) (255 * color.getBlue()),
                color.getOpacity());
    }

}
