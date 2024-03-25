package com.hgrranzi.swingum.view.gui;

import com.hgrranzi.swingum.controller.ViewController;

public class LoadGameView extends BaseView {

    public LoadGameView(ViewController controller) {
        super(controller);
        addButton("Load", e -> controller.switchView("GameView"));
        addButton("Back", e -> controller.switchView("PreviousView"));
    }
}
