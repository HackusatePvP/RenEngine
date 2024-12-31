package template.renjava;

import me.piitex.renjava.RenJava;
import me.piitex.renjava.api.saves.Save;
import me.piitex.renjava.gui.Container;
import me.piitex.renjava.gui.DisplayOrder;
import me.piitex.renjava.gui.containers.EmptyContainer;
import me.piitex.renjava.gui.menus.DefaultMainMenu;
import me.piitex.renjava.gui.menus.MainMenu;
import me.piitex.renjava.gui.overlays.ButtonOverlay;
import me.piitex.renjava.gui.overlays.ImageOverlay;
import me.piitex.renjava.gui.overlays.TextOverlay;

public class GameMenu implements MainMenu {
    // You can use the default menu as a base and modify the menus you want. There is an example for guidance.
    private final DefaultMainMenu mainMenu = new DefaultMainMenu();

    @Override
    public Container mainMenu(boolean rightClick) {
        Container container = new EmptyContainer(1920, 1080);

        ImageOverlay backgroundImage = new ImageOverlay("gui/main_menu.png");
        backgroundImage.setOrder(DisplayOrder.LOW); // Send to back of container
        container.addOverlay(backgroundImage);

        TextOverlay textOverlay = new TextOverlay(Main.getInstance().getName());
        textOverlay.setFont(RenJava.CONFIGURATION.getUiFont());
        textOverlay.setOrder(DisplayOrder.HIGH); // Move to  front of container.
        container.addOverlay(textOverlay);

        return container;
    }

    @Override
    public Container sideMenu(boolean rightClick) {
        return mainMenu.sideMenu(rightClick);
    }

    @Override
    public Container loadMenu(boolean rightClick, int page, boolean loadMenu) {
        return mainMenu.loadMenu(rightClick, page, loadMenu);
    }

    @Override
    public Container settingMenu(boolean rightClick) {
        return mainMenu.settingMenu(rightClick);
    }

    @Override
    public Container aboutMenu(boolean rightClick) {
        return mainMenu.aboutMenu(rightClick);
    }

    @Override
    public ButtonOverlay savePreview(Save save, int page, int index) {
        return mainMenu.savePreview(save, page, index);
    }
}
