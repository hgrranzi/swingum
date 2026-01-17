package com.hgrranzi.swingum.view.gui;

import com.hgrranzi.swingum.controller.GameController;
import com.hgrranzi.swingum.model.Hero;
import com.hgrranzi.swingum.model.HeroClass;
import com.hgrranzi.swingum.view.UserInterface;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GuiFrame extends JFrame implements UserInterface {

    private static int frameWidth;

    private static int frameHeight;

    public static int getFrameWidth() {
        return frameWidth;
    }

    public static int getFrameHeight() {
        return frameHeight;
    }

    private BaseView currentView;

    public GuiFrame() {
        setTitle("Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frameHeight = screenSize.height * 7 / 8;
        frameWidth = screenSize.width * 7 / 8;
        setPreferredSize(new Dimension(frameWidth, frameHeight)); // Set the preferred size
        pack(); // Pack the components to fit preferred size
        setLocationRelativeTo(null); // Center the frame on the screen
        setResizable(false);
    }

    @Override
    public void setWelcomeView(GameController controller) {
        setView(new WelcomeView(controller));
    }

    @Override
    public void setNewGameView(GameController controller, HeroClass[] heroClasses) {
        setView(new NewGameView(controller, HeroClass.values()));
    }

    @Override
    public void setLoadGameView(GameController controller, List<String> heroNames) {
        setView(new LoadGameView(controller, heroNames));
    }

    @Override
    public void setGameView(GameController controller, Hero hero) {
        setView(new GameView(controller, hero));
    }

    private void setView(BaseView view) {
        getContentPane().removeAll();
        getContentPane().add(view, BorderLayout.CENTER);
        currentView = view;
        revalidate();
        repaint();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void refreshView() {
        currentView.refresh();
    }

    @Override
    public void closeFrame() {
        dispose();
    }

    @Override
    public void displayError(String error) {
        currentView.setErrorMessage(error);
        currentView.refresh();
    }

}

