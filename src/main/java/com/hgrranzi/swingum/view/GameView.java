package com.hgrranzi.swingum.view;

import javax.swing.*;
import java.awt.event.ActionListener;

public class GameView extends JFrame {

    private JButton newGameButton, loadGameButton, saveGameButton, exitGameButton;

    public GameView() {
        setTitle("Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1024, 1024); // Adjust size based on your needs
        initializeComponents();
    }

    private void initializeComponents() {

    }

    public void addNewGameButtonListener(ActionListener listener) {
        newGameButton.addActionListener(listener);
    }

    public void addLoadGameButtonListener(ActionListener listener) {
        loadGameButton.addActionListener(listener);
    }

    public void addSaveGameButtonListener(ActionListener listener) {
        saveGameButton.addActionListener(listener);
    }

    public void addExitGameButtonListener(ActionListener listener) {
        exitGameButton.addActionListener(listener);
    }

}

