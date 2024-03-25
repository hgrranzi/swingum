package com.hgrranzi.swingum.view.gui;

import com.hgrranzi.swingum.controller.ViewController;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

@Getter
public abstract class BaseView extends JPanel {

    protected final ViewController viewController;

    protected BaseView(ViewController viewController) {
        super(new FlowLayout(FlowLayout.CENTER, 10, 10));
        this.viewController = viewController;
    }

    protected void addButton(String label, ActionListener listener) {
        JButton button = new JButton(label);
        button.addActionListener(listener);
        add(button);
    }
}

