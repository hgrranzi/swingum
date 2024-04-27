package com.hgrranzi.swingum.view;

import com.hgrranzi.swingum.controller.GameController;
import com.hgrranzi.swingum.view.gui.GuiFrame;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.List;

import static com.hgrranzi.swingum.view.gui.ImageManager.getImage;
import static com.hgrranzi.swingum.view.gui.ImageManager.scaleImage;

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
        eastPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        eastPanel.setPreferredSize(new Dimension(GuiFrame.getFrameWidth() / 4, 0));
        eastPanel.setBorder(BorderFactory.createLoweredBevelBorder());
        centerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawCenterPanel(g);
            }
        };

        add(northPanel, BorderLayout.NORTH);
        add(westPanel, BorderLayout.WEST);
        add(eastPanel, BorderLayout.EAST);
        JPanel southPanel = new JPanel();
        southPanel.setPreferredSize(northPanel.getPreferredSize());
        add(southPanel, BorderLayout.SOUTH);
        add(centerPanel, BorderLayout.CENTER);
    }

    abstract void drawCenterPanel(Graphics g);

    public void refresh() {
        revalidate();
        repaint();
    }

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

    protected ImageIcon getRadioIcon(String identifier, int iconSize) {
        return new ImageIcon(scaleImage(getImage("radio.png"), iconSize, iconSize));
    }

    protected ImageIcon getSelectedIcon(int iconSize) {
        return new ImageIcon(scaleImage(getImage("yes.png"), iconSize, iconSize));
    }

    protected void displayScrollRadioButtonList(JPanel titlePanel, List<String> identifiers, ButtonGroup group) {
        JPanel radioPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        int iconSize = (GuiFrame.getFrameHeight() - northPanel.getPreferredSize().height * 2 - 5 * 10) / 6;
        ImageIcon selectedIcon = getSelectedIcon(iconSize);

        for (String identifier : identifiers) {
            ImageIcon radioIcon = getRadioIcon(identifier, iconSize);
            JRadioButton radioButton = new JRadioButton(identifier, radioIcon);
            radioButton.setActionCommand(identifier);
            radioButton.setSelectedIcon(selectedIcon);
            radioButton.setPreferredSize(new Dimension(GuiFrame.getFrameWidth() / 2, iconSize));
            group.add(radioButton);
            radioPanel.add(radioButton);
        }

        JScrollPane scrollPane = createScrollPane(radioPanel);
        centerPanel.add(titlePanel);
        centerPanel.add(scrollPane);
    }

    private JScrollPane createScrollPane(JPanel radioPanel) {
        JScrollPane scrollPane = new JScrollPane(radioPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        scrollPane.setPreferredSize(new Dimension(GuiFrame.getFrameWidth() - westPanel.getPreferredSize().width * 2 - 4 * 10,
                                                  GuiFrame.getFrameHeight() - northPanel.getPreferredSize().height * 2 - 5 * 10));
        return scrollPane;
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

