package com.hgrranzi.swingum.view;

import com.hgrranzi.swingum.controller.GameController;

public class NewGameView extends BaseView {

    public NewGameView(GameController controller) {
        super(controller);
        addButton("Start", e -> controller.showTheView("GameView"));
        addButton("Back", e -> controller.showTheView("WelcomeView"));
    }
}
