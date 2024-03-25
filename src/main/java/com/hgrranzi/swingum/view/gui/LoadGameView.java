package com.hgrranzi.swingum.view.gui;

import com.hgrranzi.swingum.controller.ViewController;
import com.hgrranzi.swingum.view.BaseView;

public class LoadGameView extends BaseView {

    public LoadGameView(ViewController controller) {
        super(controller);
        addButton("Load", e -> controller.switchView("GameView"));
        addButton("Back", e -> controller.switchView("WelcomeView"));
    }
}
