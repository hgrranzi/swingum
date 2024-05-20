package com.hgrranzi.swingum.view.console;

import com.hgrranzi.swingum.controller.GameController;
import com.hgrranzi.swingum.model.Hero;
import com.hgrranzi.swingum.model.HeroClass;
import com.hgrranzi.swingum.view.UserInterface;

import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConsoleFrame implements UserInterface {

    private final Map<String, ActionListener> actions = new HashMap<>();

    private String nameField;

    private String heroClassField;

    @Override
    public void setWelcomeView(GameController controller) {
        System.out.println("Set Welcome view view");

        actions.put("New Game", e -> controller.switchView("NewGameView"));
        actions.put("Load Game", e -> controller.switchView("LoadGameView"));
        actions.put("Switch Interface", e -> controller.switchUserInterface());
        actions.put("Exit", e -> System.exit(0));

    }

    @Override
    public void setNewGameView(GameController controller, HeroClass[] heroClasses) {
        System.out.println("Set new game view");

        actions.put("Start", e -> controller.newGame(nameField, HeroClass.valueOf(heroClassField)));
        actions.put("Back", e -> controller.switchView("WelcomeView"));

    }

    @Override
    public void setLoadGameView(GameController controller, List<String> heroNames) {
        System.out.println("Set load game view");

        actions.put("Load", e -> controller.loadGame(nameField));
        actions.put("Back", e -> controller.switchView("WelcomeView"));
    }

    @Override
    public void setGameView(GameController controller, Hero hero) {
        System.out.println("Set game view");

        actions.put("Save game", e -> controller.saveGame());
        actions.put("Main menu", e -> controller.switchView("WelcomeView"));

        actions.put("N", e -> controller.moveHero('n'));
        actions.put("W", e -> controller.moveHero('w'));
        actions.put("E", e -> controller.moveHero('e'));
        actions.put("S", e -> controller.moveHero('s'));

        //

    }

    @Override
    public void refreshView() {
        System.out.println("Refreshing view");
    }

    @Override
    public void closeFrame() {
        ScannerProvider.closeScanner();
        System.out.println("Closing console view");
    }

    private void setView() {

    }
}
