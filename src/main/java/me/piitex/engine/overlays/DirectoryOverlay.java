package me.piitex.engine.overlays;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.DirectoryChooser;
import me.piitex.engine.Window;
import me.piitex.engine.loaders.FontLoader;
import me.piitex.engine.overlays.events.IDirectorySelect;

import java.io.File;

public class DirectoryOverlay extends Overlay {
    private final Window window;
    private IDirectorySelect directorySelect;
    private String text;
    private FontLoader fontLoader;

    public DirectoryOverlay(Window window, String text) {
        this.window = window;
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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
        Button button = new Button(text);
        if (fontLoader != null) {
            button.setFont(fontLoader.getFont());
        }

        button.setTranslateX(getX());
        button.setTranslateY(getY());

        button.setOnMouseClicked(event -> {
            DirectoryChooser chooser = new DirectoryChooser();
            File directory = chooser.showDialog(window.getStage());

            if (getDirectorySelect() != null) {
                if (directory != null) {
                    directorySelect.onDirectorySelect(new DirectorySelectEvent(directory));
                    System.out.println("Directory: " + directory.getAbsolutePath());
                }
            }

        });

        setInputControls(button);

        return button;
    }
}
