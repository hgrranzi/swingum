package com.hgrranzi.swingum.view.gui;

import com.hgrranzi.swingum.controller.GameController;
import com.hgrranzi.swingum.view.UserInterface;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;

@Getter
public class GuiFrame extends JFrame implements UserInterface {

    private static final int FRAME_WIDTH = 1024;
    private static final int FRAME_HEIGHT = 768;

    private BaseView view;
    private BaseView previousView;

    public GuiFrame() {
        setTitle("Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
    }

    @Override
    public void setView(String viewName, GameController controller) {
        switch (viewName) {
            case "WelcomeView":
                previousView = view;
                view = new WelcomeView(controller);
                break;
            case "NewGameView":
                previousView = view;
                view = new NewGameView(controller);
                break;
            case "LoadGameView":
                previousView = view;
                view = new LoadGameView(controller);
                break;
            case "LeaderboardView":
                previousView = view;
                view = new LeaderboardView(controller);
                break;
            case "GameView":
                previousView = view;
                view = new GameView(controller);
                break;
            case "PreviousView":
                view = previousView;
                break;
            default:
                throw new IllegalArgumentException("Invalid view name: " + viewName);
        }
        getContentPane().removeAll();
        getContentPane().add(view, BorderLayout.CENTER);
        revalidate();
        repaint();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void closeFrame() {
        dispose();
    }

}

