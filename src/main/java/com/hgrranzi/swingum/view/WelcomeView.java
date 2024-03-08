package com.hgrranzi.swingum.view;

import com.hgrranzi.swingum.controller.GameController;

import java.awt.*;

public class WelcomeView extends BaseView {

    public WelcomeView(GameController controller) {
        super(controller);
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        addButton("New Game", e -> controller.showTheView("NewGameView"));
        addButton("Load Game", e -> controller.showTheView("LoadGameView"));
        addButton("Leaderboard", e -> controller.showTheView("LeaderboardView"));
        addButton("Exit", e -> System.exit(0));
    }
}

