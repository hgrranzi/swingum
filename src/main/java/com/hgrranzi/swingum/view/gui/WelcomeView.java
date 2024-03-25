package com.hgrranzi.swingum.view.gui;

import com.hgrranzi.swingum.controller.ViewController;

public class WelcomeView extends BaseView {

    public WelcomeView(ViewController controller) {
        super(controller);

        addButton("New Game", e -> controller.switchView("NewGameView"));
        addButton("Load Game", e -> controller.switchView("LoadGameView"));
        addButton("Switch Interface", e -> controller.switchUserInterface());
        addButton("Exit", e -> System.exit(0));
    }
}

