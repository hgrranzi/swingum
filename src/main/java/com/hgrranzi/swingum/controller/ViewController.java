package com.hgrranzi.swingum.controller;

import com.hgrranzi.swingum.model.Hero;
import com.hgrranzi.swingum.model.HeroClass;
import com.hgrranzi.swingum.view.*;
import com.hgrranzi.swingum.view.console.ConsoleFrame;
import com.hgrranzi.swingum.view.gui.*;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ViewController {

    private UserInterface userInterface;

    public void switchView(String viewName) {
        BaseView view;
        switch (viewName) {
            case "WelcomeView":
                view = new WelcomeView(this);
                break;
            case "NewGameView":
                view = new NewGameView(this, HeroClass.values());
                break;
            case "LoadGameView":
                view = new LoadGameView(this);
                break;
            default:
                userInterface.refreshView();
                return;
        }
        userInterface.setView(view);
    }

    public void newGame(String name, HeroClass heroClass) {
        System.out.println("Creating hero " + name + " of class " + heroClass);
        GameController gameController = GameController.createGame(this, Hero.builder()
                                                                     .name(name)
                                                                     .clazz(heroClass)
                                                                     .build());
        userInterface.setView(gameController.getGameView());
    }

    public void switchUserInterface() {
        if (userInterface instanceof ConsoleFrame) {
            userInterface = new GuiFrame();
        } else {
            userInterface.closeFrame();
            userInterface = new ConsoleFrame();
        }
        switchView("WelcomeView");
    }

}

