package me.piitex.os.exceptions;

public class InvalidDownloadThreadException extends Exception {

    public InvalidDownloadThreadException() {
        super("Download was called in the JavaFX thread.");
    }
}
