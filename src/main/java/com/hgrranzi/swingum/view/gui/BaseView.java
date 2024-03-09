package com.hgrranzi.swingum.view.gui;

import com.hgrranzi.swingum.controller.GameController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public abstract class BaseView extends JPanel {

    protected final GameController controller;

    protected BaseView(GameController controller) {
        super(new FlowLayout(FlowLayout.CENTER, 10, 10));
        this.controller = controller;
    }

    protected void addButton(String label, ActionListener listener) {
        JButton button = new JButton(label);
        button.addActionListener(listener);
        add(button);
    }
}

