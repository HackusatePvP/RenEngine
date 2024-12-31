package template.renjava;

import me.piitex.renjava.RenJava;
import me.piitex.renjava.api.Game;
import me.piitex.renjava.configuration.Configuration;
import me.piitex.renjava.gui.Window;

@Game(name = "Template Project", author = "Unknown", version = "0.0.0")
@Configuration(title = "{name} v{version}", width = 1920, height = 1080)
public class Main extends RenJava {
    private static Main instance;

    public Main() {
        instance = this;
    }

    @Override
    public void preEnabled() {
        setMainMenu(new GameMenu());
    }

    @Override
    public void createBaseData() {

    }

    @Override
    public Window buildSplashScreen() {
        return null;
    }

    @Override
    public void createStory() {

    }

    @Override
    public void start() {

    }
}
