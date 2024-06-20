package com.hgrranzi.swingum.view.console;

import com.hgrranzi.swingum.controller.GameController;
import com.hgrranzi.swingum.model.Hero;
import com.hgrranzi.swingum.model.HeroClass;
import com.hgrranzi.swingum.view.UserInterface;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ConsoleFrame implements UserInterface {

    private final Map<String, ActionListener> actions = new HashMap<>();

    private final Map<String, ActionListener> gameActions = new HashMap<>();

    private final Scanner scanner = ScannerProvider.getScanner();

    private final List<String> inputArgs = new ArrayList<>(2);

    private String content = "";

    @Override
    public void setWelcomeView(GameController controller) {
        actions.clear();
        gameActions.clear();

        actions.put("NewGame", e -> controller.switchView("NewGameView"));
        actions.put("LoadGame", e -> controller.switchView("LoadGameView"));
        actions.put("SwitchInterface", e -> controller.switchUserInterface());
        actions.put("Exit", e -> System.exit(0));

        content = "";

        setView();

    }

    @Override
    public void setNewGameView(GameController controller, HeroClass[] heroClasses) {
        actions.clear();
        gameActions.clear();

        actions.put("Start", e -> controller.newGame(inputArgs.get(0), inputArgs.get(1)));
        actions.put("Back", e -> controller.switchView("WelcomeView"));

        content = "To start game enter: Start *hero name* *hero class*\n";
        content += "Available hero classes: " + Arrays.toString(heroClasses);

        setView();

    }

    @Override
    public void setLoadGameView(GameController controller, List<String> heroNames) {
        actions.clear();
        gameActions.clear();

        actions.put("Load", e -> controller.loadGame(inputArgs.get(0)));
        actions.put("Back", e -> controller.switchView("WelcomeView"));

        content = "To load game enter: Load *hero name*\n";
        content += "Available heroes to load: " + heroNames.toString();

        setView();
    }

    @Override
    public void setGameView(GameController controller, Hero hero) {
        actions.clear();
        gameActions.clear();

        gameActions.put("Save", e -> controller.saveGame());
        actions.put("Menu", e -> controller.switchView("WelcomeView"));

        // todo: implement game

        gameActions.put("N", e -> controller.moveHero('n'));
        gameActions.put("W", e -> controller.moveHero('w'));
        gameActions.put("E", e -> controller.moveHero('e'));
        gameActions.put("S", e -> controller.moveHero('s'));

        content = "";

        setView();

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

    @Override
    public void displayError(String error) {
        System.out.println("Error: " + error);
        setView();
    }

    private void setView() {
        System.out.println("Choose an option: " + actions.keySet() + gameActions.keySet());
        System.out.println(content);
        for (String[] input = scanner.nextLine().split("\\s"); ; input = scanner.nextLine().split("\\s")) {
            // todo: handle crl + D
            if (actions.containsKey(input[0])) {
                inputArgs.clear();
                inputArgs.add(input.length > 1 ? input[1] : "");
                inputArgs.add(input.length > 2 ? input[2] : "");
                actions.get(input[0]).actionPerformed(null);
                break;
            } else if (gameActions.containsKey(input[0])) {
                inputArgs.clear();
                gameActions.get(input[0]).actionPerformed(null);
            }
            System.out.println("Choose an option: " + actions.keySet() + gameActions.keySet());
            System.out.println(content);
        }
    }
}
