package com.hgrranzi.swingum.view;

import lombok.Getter;

import javax.swing.*;
import java.awt.*;

@Getter
public class GameFrame extends JFrame {

    private static final int FRAME_WIDTH = 1024;
    private static final int FRAME_HEIGHT = 768;

    private BaseView view;

    public GameFrame() {
        setTitle("Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
    }

    public void setView(BaseView view) {
        getContentPane().removeAll();
        getContentPane().add(view, BorderLayout.CENTER);
        this.view = view;
        revalidate();
        repaint();
        setLocationRelativeTo(null);
        setVisible(true);
    }

}

