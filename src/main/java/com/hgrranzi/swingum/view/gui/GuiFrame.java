package com.hgrranzi.swingum.view.gui;

import com.hgrranzi.swingum.view.BaseView;
import com.hgrranzi.swingum.view.UserInterface;

import javax.swing.*;
import java.awt.*;

public class GuiFrame extends JFrame implements UserInterface {

    private static final int FRAME_WIDTH = 1024;
    private static final int FRAME_HEIGHT = 768;

    public GuiFrame() {
        setTitle("Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
    }

    @Override
    public void setView(BaseView view) {
        view.displayGuiButtons();
        getContentPane().removeAll();
        getContentPane().add(view, BorderLayout.CENTER);
        revalidate();
        repaint();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void refreshView() {
        revalidate();
        repaint();
    }

    @Override
    public void closeFrame() {
        dispose();
    }

}

