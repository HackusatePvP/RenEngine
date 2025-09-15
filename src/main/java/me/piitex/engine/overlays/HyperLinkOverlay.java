package me.piitex.engine.overlays;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.Border;
import me.piitex.engine.loaders.FontLoader;

public class HyperLinkOverlay extends Overlay {
    private final Hyperlink hyperlink;
    private final String link;
    private String text;
    private FontLoader font;

    public HyperLinkOverlay(String link) {
        this.hyperlink = new Hyperlink(link);
        this.link = link;
        setNode(hyperlink);
    }

    public HyperLinkOverlay(String link, String text) {
        this.hyperlink = new Hyperlink(text);
        this.link = link;
        this.text = text;
        setNode(hyperlink);
    }

    public String getLink() {
        return link;
    }

    public FontLoader getFontLoader() {
        return font;
    }

    public void setFont(FontLoader font) {
        this.font = font;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        hyperlink.setText(text);
    }

    @Override
    public Node render() {
        if (font != null) {
            hyperlink.setFont(font.getFont());
        }
        hyperlink.setBorder(Border.EMPTY);
        hyperlink.setPadding(new Insets(4,0,4,0));
        hyperlink.setTranslateX(getX());
        hyperlink.setTranslateY(getY());
        hyperlink.setOnMouseClicked(event -> {

        });
        setInputControls(hyperlink);
        return hyperlink;
    }
}
