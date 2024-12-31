package me.piitex.engine.overlays;

import me.piitex.engine.hanlders.events.Event;

import java.io.File;

public class DirectorySelectEvent extends Event {
    private final File directory;

    public DirectorySelectEvent(File directory) {
        this.directory = directory;
    }

    public File getDirectory() {
        return directory;
    }
}
