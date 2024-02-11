package com.hgrranzi.swingum;

import com.hgrranzi.swingum.controller.GameController;
import com.hgrranzi.swingum.model.Game;
import com.hgrranzi.swingum.view.GameView;

import javax.swing.*;

public class SwingumApplication {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Game game = new Game();
            GameView gameView = new GameView();
            GameController gameController = new GameController(game, gameView);
            gameView.setVisible(true);
        });

    }
}
