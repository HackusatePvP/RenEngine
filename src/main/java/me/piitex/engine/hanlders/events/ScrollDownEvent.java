package me.piitex.engine.hanlders.events;

import me.piitex.renjava.events.Cancellable;

public class ScrollDownEvent extends Event implements Cancellable {
    private boolean cancel = false;
    @Override
    public void setCancelled(boolean cancelled) {
        this.cancel = cancelled;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }
}
