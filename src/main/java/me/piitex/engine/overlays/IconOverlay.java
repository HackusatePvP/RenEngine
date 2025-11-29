package me.piitex.engine.overlays;

import javafx.scene.Node;
import javafx.scene.paint.Paint;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.javafx.FontIcon;

/**
 * RenEngine wrapper for {@link FontIcon}. Style classes are disabled for this overlay.
 */
public class IconOverlay extends Overlay {
    private final FontIcon fontIcon;
    private String iconCode;
    private int iconSize = 16;
    private Paint color;

    public IconOverlay() {
        this.fontIcon = new FontIcon();
        setNode(fontIcon);
    }

    public IconOverlay(Ikon ikon) {
        this.fontIcon = new FontIcon(ikon);
        setNode(fontIcon);
    }

    public IconOverlay(String iconCode) {
        this.fontIcon = new FontIcon(iconCode);
        this.iconCode = iconCode;
        setNode(fontIcon);
    }

    public void setIconCode(String iconCode) {
        this.iconCode = iconCode;
        fontIcon.setIconLiteral(iconCode);
    }

    public int getIconSize() {
        return iconSize;
    }

    public void setIconSize(int iconSize) {
        this.iconSize = iconSize;
        fontIcon.setIconSize(iconSize);
    }

    public Paint getColor() {
        return color;
    }

    public void setColor(Paint color) {
        this.color = color;
        fontIcon.setIconColor(color);
    }

    @Override
    protected Node render() {
        fontIcon.getStyleClass().clear();
        fontIcon.setTranslateX(getX());
        fontIcon.setTranslateY(getY());
        if (iconCode != null) {
            fontIcon.setIconLiteral(iconCode);
        }
        fontIcon.setIconSize(iconSize);
        if (color != null) {
            fontIcon.setIconColor(color);
        }
        return fontIcon;
    }

    public FontIcon getFontIcon() {
        return fontIcon;
    }
}