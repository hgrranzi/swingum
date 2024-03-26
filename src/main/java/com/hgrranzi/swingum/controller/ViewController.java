package com.hgrranzi.swingum.controller;

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
                view = new NewGameView(this);
                break;
            case "LoadGameView":
                view = new LoadGameView(this);
                break;
            case "GameView":
                GameController gameController = new GameController(this);
                view = gameController.getGameView();
                break;
            default:
                userInterface.refreshView();
                return;
        }
        userInterface.setView(view);
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

