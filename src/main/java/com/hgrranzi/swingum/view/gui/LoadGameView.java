package com.hgrranzi.swingum.view.gui;

import com.hgrranzi.swingum.controller.GameController;

public class LoadGameView extends BaseView {

    public LoadGameView(GameController controller) {
        super(controller);
        addButton("Load", e -> controller.switchView("GameView"));
        addButton("Back", e -> controller.switchView("PreviousView"));
    }
}
