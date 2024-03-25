package com.hgrranzi.swingum.view.gui;

import com.hgrranzi.swingum.controller.ViewController;

public class NewGameView extends BaseView {

    public NewGameView(ViewController controller) {
        super(controller);
        addButton("Start", e -> controller.switchView("GameView"));
        addButton("Back", e -> controller.switchView("PreviousView"));
    }
}
