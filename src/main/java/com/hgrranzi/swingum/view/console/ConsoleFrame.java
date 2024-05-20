package com.hgrranzi.swingum.view.console;

import com.hgrranzi.swingum.controller.GameController;
import com.hgrranzi.swingum.model.Hero;
import com.hgrranzi.swingum.model.HeroClass;
import com.hgrranzi.swingum.view.UserInterface;

import java.util.List;

public class ConsoleFrame implements UserInterface {

    @Override
    public void setWelcomeView(GameController controller) {
        System.out.println("Set Welcome view view");
    }

    @Override
    public void setNewGameView(GameController controller, HeroClass[] heroClasses) {
        System.out.println("Set new game view");
    }

    @Override
    public void setLoadGameView(GameController controller, List<String> heroNames) {
        System.out.println("RSet load game view");
    }

    @Override
    public void setGameView(GameController controller, Hero hero) {
        System.out.println("RSet game view");
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
}
