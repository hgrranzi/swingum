package com.hgrranzi.swingum.view.gui;

import com.hgrranzi.swingum.controller.GameController;

public class WelcomeView extends BaseView {

    public WelcomeView(GameController controller) {
        super(controller);

        addButton("New Game", e -> controller.switchView("NewGameView"));
        addButton("Load Game", e -> controller.switchView("LoadGameView"));
        addButton("Leaderboard", e -> controller.switchView("LeaderboardView"));
        addButton("Switch Interface", e -> controller.switchUserInterface());
        addButton("Exit", e -> System.exit(0));
    }
}

