package com.hgrranzi.swingum.view;

import com.hgrranzi.swingum.controller.GameController;

public class WelcomeView extends BaseView {

    public WelcomeView(GameController controller) {
        super(controller);

        addButton("New Game", e -> controller.showTheView("NewGameView"));
        addButton("Load Game", e -> controller.showTheView("LoadGameView"));
        addButton("Leaderboard", e -> controller.showTheView("LeaderboardView"));
        addButton("Exit", e -> System.exit(0));
    }
}

