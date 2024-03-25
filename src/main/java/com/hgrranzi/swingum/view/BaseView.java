package com.hgrranzi.swingum.view;

import com.hgrranzi.swingum.controller.ViewController;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;
import java.util.Map;

@Getter
public abstract class BaseView extends JPanel {

    protected final ViewController viewController;

    protected final Map<String, ActionListener> buttons = new LinkedHashMap<>();

    protected BaseView(ViewController viewController) {
        super(new FlowLayout(FlowLayout.CENTER, 10, 10));
        this.viewController = viewController;
    }

    protected void addButton(String label, ActionListener listener) {
        buttons.put(label, listener);
    }

    public void displayGuiButtons() {
        for (Map.Entry<String, ActionListener> entry : buttons.entrySet()) {
            JButton button = new JButton(entry.getKey());
            button.addActionListener(entry.getValue());
            add(button);
        }
    }

    public void displayConsoleButtons() {
        // todo: add to buffer to print when print all the view ?
        System.out.print("| ");
        for (Map.Entry<String, ActionListener> entry : buttons.entrySet()) {
            System.out.print(entry.getKey() + " | ");
        }
        System.out.println();
    }
}

