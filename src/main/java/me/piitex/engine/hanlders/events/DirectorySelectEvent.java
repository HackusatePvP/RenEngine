package me.piitex.engine.hanlders.events;

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
