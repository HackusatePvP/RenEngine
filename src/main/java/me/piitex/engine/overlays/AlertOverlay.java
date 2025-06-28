package me.piitex.engine.overlays;

import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import me.piitex.engine.Window;
import me.piitex.engine.hanlders.events.AlertConfirmEvent;
import me.piitex.engine.overlays.events.IAlertConfirm;

import java.util.ArrayList;
import java.util.List;

public class AlertOverlay extends Overlay implements Region {
    private final String title;
    private final Alert.AlertType alertType;
    private String header;
    private String content;
    private double width, height, prefWidth, prefHeight, maxWidth, maxHeight;
    private double scaleWidth, scaleHeight;
    private Window modal;

    private List<ButtonType> actionButtons = new ArrayList<>();

    private IAlertConfirm alertConfirm;

    public AlertOverlay(String title, Alert.AlertType alertType) {
        this.title = title;
        this.alertType = alertType;
    }

    public String getTitle() {
        return title;
    }

    public Alert.AlertType getAlertType() {
        return alertType;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Window getModal() {
        return modal;
    }

    public void setModal(Window modal) {
        this.modal = modal;
    }

    public void addButton(ButtonType buttonType) {
        actionButtons.add(buttonType);
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
    }

    @Override
    public void setPrefHeight(double h) {
        this.prefHeight = h;
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
    }

    @Override
    public void setMaxHeight(double h) {
        this.maxHeight = h;
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

    public IAlertConfirm getAlertConfirm() {
        return alertConfirm;
    }

    public void onConfirm(IAlertConfirm alertConfirm) {
        this.alertConfirm = alertConfirm;
    }

    public Alert build() {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        if (modal != null) {
            alert.initOwner(modal.getStage());
        }
        alert.setX(getX());
        alert.setY(getY());
        alert.setWidth(getWidth());
        alert.setHeight(getHeight());

        alert.getButtonTypes().setAll(actionButtons);

        if (getAlertConfirm() != null) {
            alert.setOnCloseRequest(event -> {
                getAlertConfirm().onConfirm(new AlertConfirmEvent(alert, this));
            });
        }
        return alert;
    }

    @Override
    public Node render() {
        return null;
    }
}
