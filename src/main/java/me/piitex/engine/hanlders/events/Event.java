package me.piitex.engine.hanlders.events;

public abstract class Event {
    private boolean sync = true;

    public void setSync(boolean sync) {
        this.sync = sync;
    }

    public boolean isSync() {
        return sync;
    }
}