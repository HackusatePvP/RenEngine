package me.piitex.engine.hanlders.events;

public class ScrollUpEvent extends Event implements Cancellable {
    private boolean displayPreviousScene = true;
    private boolean cancel = false;

    public boolean isDisplayPreviousScene() {
        return displayPreviousScene;
    }

    public void setDisplayPreviousScene(boolean displayPreviousScene) {
        this.displayPreviousScene = displayPreviousScene;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancel = cancelled;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }
}
