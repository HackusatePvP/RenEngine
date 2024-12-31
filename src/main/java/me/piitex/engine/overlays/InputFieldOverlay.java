package me.piitex.engine.overlays;

import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import me.piitex.engine.hanlders.events.InputSetEvent;
import me.piitex.engine.loaders.FontLoader;
import me.piitex.engine.overlays.events.IInputSetEvent;

public class InputFieldOverlay extends Overlay implements Region {
    private double width, height;
    private double scaleWidth, scaleHeight;
    private final String defaultInput;
    private FontLoader fontLoader;
    private String hintText = "";
    private String currentText;
    private Color textFill = Color.BLACK;
    private boolean renderBorder = true;

    private IInputSetEvent iInputSetEvent;

    public InputFieldOverlay(String defaultInput, double x, double y, double width, double height) {
        this.defaultInput = defaultInput;
        this.width = width;
        this.height = height;
        setX(x);
        setY(y);
    }

    public InputFieldOverlay(String defaultInput, String hintText, double x, double y, double width, double height) {
        this.defaultInput = defaultInput;
        this.hintText = hintText;
        this.width = width;
        this.height = height;
        setX(x);
        setY(y);
    }

    public FontLoader getFontLoader() {
        return fontLoader;
    }

    public void setFont(FontLoader fontLoader) {
        this.fontLoader = fontLoader;
    }

    public String getCurrentText() {
        return currentText;
    }

    public Color getTextFill() {
        return textFill;
    }

    public void setTextFill(Color textFill) {
        this.textFill = textFill;
    }

    public boolean isRenderBorder() {
        return renderBorder;
    }

    public void setRenderBorder(boolean renderBorder) {
        this.renderBorder = renderBorder;
    }

    @Override
    public Node render() {
        TextField textField = build();
        textField.setPromptText(hintText);
        textField.setText(defaultInput);

        if (getiInputSetEvent() != null) {
            getiInputSetEvent().onInputSet(new InputSetEvent(this, defaultInput));
        }


        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            iInputSetEvent.onInputSet(new InputSetEvent(this, newValue));
        });
        setInputControls(textField);
        return textField;
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
    }

    @Override
    public void setHeight(double h) {
        this.height = h;
    }

    @Override
    public double getScaleWidth() {
        return scaleWidth;
    }

    @Override
    public void setScaleWidth(double w) {
        this.scaleWidth = w;
    }

    @Override
    public double getScaleHeight() {
        return scaleHeight;
    }

    @Override
    public void setScaleHeight(double h) {
        this.scaleHeight = h;
    }

    public IInputSetEvent getiInputSetEvent() {
        return iInputSetEvent;
    }

    public void onInputSetEvent(IInputSetEvent iInputSetEvent) {
        this.iInputSetEvent = iInputSetEvent;
    }

    public TextField build() {
        TextField textField = new TextField();
        textField.setTranslateX(getX());
        textField.setTranslateY(getY());
        if (fontLoader != null) {
            textField.setFont(getFontLoader().getFont());
        }
        String inLine = "";
        if (!renderBorder) {
            System.out.println("Not rendering border.");
            inLine += "-fx-control-inner-background: transparent; -fx-background-color: transparent; ";
        }
        inLine += "-fx-text-fill: " + cssColor(textFill) + "; ";
        textField.setStyle(inLine);
        textField.setPrefSize(width, height);
        return textField;
    }

    private String cssColor(Color color) {
        return String.format("rgba(%d, %d, %d, %f)",
                (int) (255 * color.getRed()),
                (int) (255 * color.getGreen()),
                (int) (255 * color.getBlue()),
                color.getOpacity());
    }
}
