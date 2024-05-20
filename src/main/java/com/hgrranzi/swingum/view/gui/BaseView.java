package com.hgrranzi.swingum.view.gui;

import com.hgrranzi.swingum.controller.GameController;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

import static com.hgrranzi.swingum.view.gui.ImageManager.getImage;
import static com.hgrranzi.swingum.view.gui.ImageManager.scaleImage;

@Getter
public abstract class BaseView extends JPanel {

    protected final GameController gameController;

    protected final JPanel northPanel;

    protected final JPanel westPanel;

    protected final JPanel centerPanel;

    protected final JPanel eastPanel;

    protected BaseView(GameController gameController) {
        super(new BorderLayout(10, 10));
        this.gameController = gameController;

        northPanel = createNorthPanel();
        westPanel = createSidePanel();
        eastPanel = createSidePanel();
        centerPanel = createCenterPanel();

        addComponentsToPanel();
    }

    private JPanel createNorthPanel() {
        return new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
    }

    private JPanel createSidePanel() {
        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.setPreferredSize(new Dimension(GuiFrame.getFrameWidth() / 4, 0));
        panel.setBorder(BorderFactory.createLoweredBevelBorder());
        return panel;
    }

    private JPanel createCenterPanel() {
        return new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawCenterPanel(g);
            }
        };
    }

    private void addComponentsToPanel() {
        add(northPanel, BorderLayout.NORTH);
        add(westPanel, BorderLayout.WEST);
        add(eastPanel, BorderLayout.EAST);
        add(createSouthPanel(), BorderLayout.SOUTH);
        add(centerPanel, BorderLayout.CENTER);
    }

    private JPanel createSouthPanel() {
        JPanel southPanel = new JPanel();
        southPanel.setPreferredSize(northPanel.getPreferredSize());
        return southPanel;
    }

    abstract void drawCenterPanel(Graphics g);

    public void refresh() {
        revalidate();
        repaint();
    }

    protected void addButton(String label, ActionListener listener) {
        JButton button = new JButton(label);
        button.addActionListener(listener);
        northPanel.add(button);
    }

    protected ImageIcon getRadioIcon(String identifier, int iconSize) {
        return new ImageIcon(scaleImage(getImage("radio.png"), iconSize, iconSize));
    }

    protected ImageIcon getSelectedIcon(int iconSize) {
        return new ImageIcon(scaleImage(getImage("yes.png"), iconSize, iconSize));
    }

    protected void createScrollRadioButtonList(JPanel titlePanel, List<String> identifiers, ButtonGroup group) {
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

}

