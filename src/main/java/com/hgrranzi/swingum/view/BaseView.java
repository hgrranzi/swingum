package com.hgrranzi.swingum.view;

import com.hgrranzi.swingum.controller.GameController;
import com.hgrranzi.swingum.view.gui.GuiFrame;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;
import java.util.Map;

@Getter
public abstract class BaseView extends JPanel {

    protected final StringBuilder viewBuffer = new StringBuilder();

    protected final GameController gameController;

    protected final Map<String, ActionListener> buttons = new LinkedHashMap<>();

    protected final JPanel northPanel;

    protected final JPanel westPanel;

    protected final JPanel centerPanel;

    protected final JPanel eastPanel;

    protected BaseView(GameController gameController) {
        super(new BorderLayout(10, 10));
        this.gameController = gameController;

        northPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        westPanel = new JPanel(new BorderLayout(10, 10));
        westPanel.setPreferredSize(new Dimension(GuiFrame.getFrameWidth() / 4, 0));
        westPanel.setBorder(BorderFactory.createLoweredBevelBorder());
        eastPanel = new JPanel(new GridLayout(3, 1));
        eastPanel.setPreferredSize(new Dimension(GuiFrame.getFrameWidth() / 4, 0));
        eastPanel.setBorder(BorderFactory.createLoweredBevelBorder());
        centerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawCenterPanel(g);
            }
        };

        add(northPanel, BorderLayout.NORTH);
        add(westPanel, BorderLayout.WEST);
        add(eastPanel, BorderLayout.EAST);
        add(centerPanel, BorderLayout.CENTER);

        JPanel southPanel = new JPanel();
        southPanel.setPreferredSize(northPanel.getPreferredSize());
        add(southPanel, BorderLayout.SOUTH);
    }

    abstract void drawCenterPanel(Graphics g);

    protected void addButton(String label, ActionListener listener) {
        buttons.put(label, listener);
    }

    public ActionListener getButtonListener(String label) {
        if (!buttons.containsKey(label)) {
            throw new SwingumException("No option: | " + label + " | available.");
        }
        return buttons.get(label);
    }

    public void displayGuiButtons() {
        for (Map.Entry<String, ActionListener> entry : buttons.entrySet()) {
            JButton button = new JButton(entry.getKey());
            button.addActionListener(entry.getValue());
            northPanel.add(button);
        }
    }

    public void displayConsoleButtons() {
        viewBuffer.delete(0, viewBuffer.length());
        viewBuffer.append("| ");
        for (Map.Entry<String, ActionListener> entry : buttons.entrySet()) {
            viewBuffer.append(entry.getKey()).append(" | ");
        }
        viewBuffer.append("\n");
        System.out.println(viewBuffer);
    }

    public void printView() {
        System.out.println(viewBuffer);
    }
}

