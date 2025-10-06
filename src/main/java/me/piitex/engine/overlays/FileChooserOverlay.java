package me.piitex.engine.overlays;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import me.piitex.engine.Window;
import me.piitex.engine.loaders.FontLoader;
import me.piitex.engine.hanlders.events.DirectorySelectEvent;
import me.piitex.engine.overlays.events.IDirectorySelect;

import java.io.File;

public class FileChooserOverlay extends Overlay {
    private final Window window;
    private IDirectorySelect directorySelect;
    private String text;
    private ButtonOverlay button;
    private FontLoader fontLoader;

    public FileChooserOverlay(Window window, String text) {
        this.window = window;
        this.text = text;
    }

    public FileChooserOverlay(Window window, ButtonOverlay button) {
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

    public IDirectorySelect getFileSelect() {
        return directorySelect;
    }

    public void onFileSelect(IDirectorySelect iDirectorySelect) {
        this.directorySelect = iDirectorySelect;
    }

    @Override
    public Node render() {
        Button jfxButton;
        if (button != null) {
            jfxButton = (Button) button.render();
        } else {
            jfxButton = new Button(text);
            jfxButton.setTranslateX(getX());
            jfxButton.setTranslateY(getY());
        }

        jfxButton.setOnMouseClicked(event -> {
            FileChooser chooser = new FileChooser();
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Select character card", "*.png"));
            File directory = chooser.showOpenDialog(window.getStage());

            if (getFileSelect() != null) {
                if (directory != null) {
                    directorySelect.onDirectorySelect(new DirectorySelectEvent(directory));
                }
            }

        });
        return jfxButton;
    }
}
