package com.hgrranzi.swingum;

import com.hgrranzi.swingum.controller.GameController;
import com.hgrranzi.swingum.model.Game;
import com.hgrranzi.swingum.view.GameFrame;

import javax.swing.*;

public class SwingumApplication {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Game game = new Game();
            GameFrame gameFrame = new GameFrame();
            GameController gameController = new GameController(gameFrame);
            gameController.showTheView("WelcomeView");
        });
    }
}
