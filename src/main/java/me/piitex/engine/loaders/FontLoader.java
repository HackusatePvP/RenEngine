package me.piitex.engine.loaders;

import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class FontLoader {
    private final String name;
    private double size;
    private Font font;
    private FontWeight weight;
    private FontPosture posture;

    public FontLoader(FontLoader font, double size) {
        this.name = font.getName();
        this.size = size;
        this.font = Font.font(font.getFont().getFamily(), size);
    }

    public FontLoader(Font font, double size) {
        this.name = font.getName();
        this.size = size;
        this.font = Font.font(font.getFamily(), size);
    }

    public FontLoader(Font font, FontWeight weight, double size) {
        this.name = font.getName();
        this.size = size;
        this.weight = weight;
        this.font = Font.font(font.getFamily(), weight, size);
    }

    public FontLoader(Font font, FontPosture posture, double size) {
        this.name = font.getName();
        this.size = size;
        this.posture = posture;
        this.font = Font.font(font.getFamily(), posture, size);
    }

    public FontLoader(Font font, FontWeight weight, FontPosture posture, double size) {
        this.name = font.getName();
        this.size = size;
        this.posture = posture;
        this.weight = weight;
        this.font = Font.font(font.getFamily(), weight, posture, size);
    }

    public FontLoader(String name, double size) {
        this.name = name;
        File directory = new File(System.getProperty("user.dir") + "/game/fonts/");
        File file = new File(directory, name);
        this.size = size;
        try {
            this.font = Font.loadFont(new FileInputStream(file), size);
        } catch (FileNotFoundException e) {
            URL resource = FontLoader.class.getClassLoader().getResource(name);
            if (resource != null) {
                this.font = Font.loadFont(resource.toExternalForm(), size);
            }
        }
    }

    public FontLoader(String name) {
        File directory = new File(System.getProperty("user.dir") + "/game/fonts/");
        File file = new File(directory, name);
        this.name = name;
        try {
            this.font = Font.loadFont(new FileInputStream(file), 24);
        } catch (FileNotFoundException e) {
            URL resource = FontLoader.class.getClassLoader().getResource(name);
            if (resource != null) {
                this.font = Font.loadFont(resource.toExternalForm(), size);
            }
        }
        this.size = 24;
    }

    public FontLoader(File file, FontPosture posture, FontWeight weight) {
        this.name = file.getName();
        try {
            this.font = Font.loadFont(new FileInputStream(file), 24);
        } catch (FileNotFoundException e) {
            URL resource = FontLoader.class.getClassLoader().getResource(name);
            if (resource != null) {
                this.font = Font.loadFont(resource.toExternalForm(), size);
            }
        }
    }

    public FontLoader(File file, FontPosture posture, FontWeight weight, double size) {
        this.name = file.getName();
        try {
            Font f = Font.loadFont(new FileInputStream(file), size);
            this.font = Font.font(f.getFamily(), weight, posture, size);

        } catch (FileNotFoundException e) {
            URL resource = FontLoader.class.getClassLoader().getResource(name);
            if (resource != null) {
                Font f = Font.loadFont(resource.toExternalForm(), size);
                this.font = Font.font(f.getFamily(), weight, posture, size);
            }
        }
    }

    public String getName() {
        return name;
    }

    public double getSize() {
        return size;
    }

    public FontPosture getPosture() {
        return posture;
    }

    public FontWeight getWeight() {
        return weight;
    }

    public void setSize(double size) {
        this.size = size;
        this.font = Font.font(name, size);
    }

    public Font getFont() {
        return font;
    }
}
