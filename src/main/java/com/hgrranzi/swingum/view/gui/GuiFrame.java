package com.hgrranzi.swingum.view.gui;

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
    public void setView(BaseView view) {
        this.previousView = this.view;
        this.view = view;
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

