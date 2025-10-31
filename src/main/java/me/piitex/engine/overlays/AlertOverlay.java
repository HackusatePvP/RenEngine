package me.piitex.engine.overlays;

import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import me.piitex.engine.Window;
import me.piitex.engine.hanlders.events.AlertConfirmEvent;
import me.piitex.engine.overlays.events.IAlertConfirm;

import java.util.ArrayList;
import java.util.List;

public class AlertOverlay extends Overlay {
    private final Alert alert;
    private final String title;
    private final Alert.AlertType alertType;
    private String header;
    private String content;
    private double width, height;
    private Window modal;

    private final List<ButtonType> actionButtons = new ArrayList<>();

    private IAlertConfirm alertConfirm;

    public AlertOverlay(String title, Alert.AlertType alertType) {
        this.alert = new Alert(alertType);
        this.title = title;
        this.alertType = alertType;
        setNode(alert.getDialogPane());
    }

    public Alert getAlert() {
        return alert;
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
        alert.setHeaderText(header);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
        alert.setContentText(content);
    }

    public Window getModal() {
        return modal;
    }

    public void setModal(Window modal) {
        this.modal = modal;
        alert.initOwner(modal.getStage());
    }

    public void addButton(ButtonType buttonType) {
        actionButtons.add(buttonType);
        alert.getButtonTypes().add(buttonType);
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public void setWidth(double w) {
        this.width = w;
        alert.setWidth(w);
    }

    public void setHeight(double h) {
        this.height = h;
        alert.setHeight(h);
    }

    public IAlertConfirm getAlertConfirm() {
        return alertConfirm;
    }

    public void onConfirm(IAlertConfirm alertConfirm) {
        this.alertConfirm = alertConfirm;
    }

    @Override
    public Node render() {
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
            alert.setOnCloseRequest(event -> getAlertConfirm().onConfirm(new AlertConfirmEvent(alert, this)));
        }
        return alert.getDialogPane();
    }
}
