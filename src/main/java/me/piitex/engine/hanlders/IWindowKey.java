package me.piitex.engine.hanlders;

import me.piitex.engine.hanlders.events.WindowKeyPressEvent;
import me.piitex.engine.hanlders.events.WindowKeyReleaseEvent;

public interface IWindowKey {

    void onKeyPress(WindowKeyPressEvent event);

    void onKeyRelease(WindowKeyReleaseEvent event);
}
