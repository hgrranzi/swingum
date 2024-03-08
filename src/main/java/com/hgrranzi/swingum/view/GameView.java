package com.hgrranzi.swingum.view;

import com.hgrranzi.swingum.controller.GameController;

public class GameView extends BaseView {

        public GameView(GameController controller) {
            super(controller);
            addButton("Save game", e -> System.out.println("Game saved"));
            addButton("Load game", e -> controller.showTheView("LoadGameView"));
            addButton("Main menu", e -> controller.showTheView("WelcomeView"));
        }
}
