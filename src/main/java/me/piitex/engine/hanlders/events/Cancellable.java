package me.piitex.engine.hanlders.events;

public interface Cancellable {

    void setCancelled(boolean cancelled);

    boolean isCancelled();
}
