package com.hgrranzi.swingum.view.gui;

import com.hgrranzi.swingum.view.BaseView;
import com.hgrranzi.swingum.view.UserInterface;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;

public class GuiFrame extends JFrame implements UserInterface {

    @Getter
    private static int frameWidth;

    @Getter
    private static int frameHeight;

    public GuiFrame() {
        setTitle("Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frameHeight = screenSize.height * 3 / 4;
        frameWidth = screenSize.width * 3 / 4;
        setPreferredSize(new Dimension(frameWidth, frameHeight)); // Set the preferred size
        pack(); // Pack the components to fit preferred size
        setLocationRelativeTo(null); // Center the frame on the screen
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

