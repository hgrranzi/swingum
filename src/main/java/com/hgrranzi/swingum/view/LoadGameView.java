package com.hgrranzi.swingum.view;

import com.hgrranzi.swingum.controller.GameController;

public class LoadGameView extends BaseView {

    public LoadGameView(GameController controller, BaseView previousView) {
        super(controller);
        addButton("Load", e -> controller.showTheView("GameView"));
        addButton("Back", e -> controller.showTheView(previousView));
    }
}
