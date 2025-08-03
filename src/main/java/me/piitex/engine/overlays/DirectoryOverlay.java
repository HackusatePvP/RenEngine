package me.piitex.engine.overlays;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.DirectoryChooser;
import me.piitex.engine.Window;
import me.piitex.engine.loaders.FontLoader;
import me.piitex.engine.hanlders.events.DirectorySelectEvent;
import me.piitex.engine.overlays.events.IDirectorySelect;

import java.io.File;

public class DirectoryOverlay extends Overlay {
    private final Window window;
    private IDirectorySelect directorySelect;
    private String text;
    private ButtonOverlay button;
    private FontLoader fontLoader;

    public DirectoryOverlay(Window window, String text) {
        this.window = window;
        this.text = text;
    }

    public DirectoryOverlay(Window window, ButtonOverlay button) {
        this.window = window;
        this.button = button;
    }

    public String getText() {
        return text;
    }

    public ButtonOverlay getButton() {
        return button;
    }

    public FontLoader getFontLoader() {
        return fontLoader;
    }

    public void setFontLoader(FontLoader fontLoader) {
        this.fontLoader = fontLoader;
    }

    public IDirectorySelect getDirectorySelect() {
        return directorySelect;
    }

    public void onDirectorySelect(IDirectorySelect iDirectorySelect) {
        this.directorySelect = iDirectorySelect;
    }

    @Override
    public Node render() {
        Button jfxButton;
        if (button != null) {
            jfxButton = (Button) button.render();
        } else {
            jfxButton = new Button(text);
        }

        jfxButton.setTranslateX(getX());
        jfxButton.setTranslateY(getY());

        jfxButton.setOnMouseClicked(event -> {
            DirectoryChooser chooser = new DirectoryChooser();
            File directory = chooser.showDialog(window.getStage());

            if (getDirectorySelect() != null) {
                if (directory != null) {
                    directorySelect.onDirectorySelect(new DirectorySelectEvent(directory));
                }
            }

        });

        setInputControls(jfxButton);

        return jfxButton;

    }
}
