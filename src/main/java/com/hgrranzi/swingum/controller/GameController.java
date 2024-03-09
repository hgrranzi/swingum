package com.hgrranzi.swingum.controller;

import com.hgrranzi.swingum.view.*;
import com.hgrranzi.swingum.view.console.ConsoleFrame;
import com.hgrranzi.swingum.view.gui.GuiFrame;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GameController {

    private UserInterface userInterface;

    public void switchView(String viewName) {
        userInterface.setView(viewName, this);
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

