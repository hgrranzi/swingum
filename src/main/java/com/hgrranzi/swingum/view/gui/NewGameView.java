package com.hgrranzi.swingum.view.gui;

import com.hgrranzi.swingum.controller.GameController;

public class NewGameView extends BaseView {

    public NewGameView(GameController controller) {
        super(controller);
        addButton("Start", e -> controller.switchView("GameView"));
        addButton("Back", e -> controller.switchView("PreviousView"));
    }
}
