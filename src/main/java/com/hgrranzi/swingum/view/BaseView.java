package com.hgrranzi.swingum.view;

import com.hgrranzi.swingum.controller.GameController;
import lombok.AllArgsConstructor;

import javax.swing.*;
import java.awt.event.ActionListener;

@AllArgsConstructor
public abstract class BaseView extends JPanel {

    protected GameController controller;

    protected void addButton(String label, ActionListener listener) {
        JButton button = new JButton(label);
        button.addActionListener(listener);
        add(button);
    }
}

