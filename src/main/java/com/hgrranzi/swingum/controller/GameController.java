package com.hgrranzi.swingum.controller;

import com.hgrranzi.swingum.view.*;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GameController {

    private GameFrame frame;

    public void showTheView(String viewName) {
        BaseView view;
        switch (viewName) {
            case "WelcomeView":
                view = new WelcomeView(this);
                break;
            case "NewGameView":
                view = new NewGameView(this);
                break;
            case "LoadGameView":
                view = new LoadGameView(this, frame.getView());
                break;
            case "LeaderboardView":
                view = new LeaderboardView(this, frame.getView());
                break;
            case "GameView":
                view = new GameView(this);
                break;
            default:
                throw new IllegalArgumentException("Invalid view name: " + viewName);
        }
        frame.setView(view);
    }

    public void showTheView(BaseView view) {
        frame.setView(view);
    }

}

