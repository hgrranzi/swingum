package com.hgrranzi.swingum.view;

import com.hgrranzi.swingum.controller.GameController;
import com.hgrranzi.swingum.model.Artefact;
import com.hgrranzi.swingum.model.ArtefactType;
import com.hgrranzi.swingum.model.Hero;
import com.hgrranzi.swingum.model.Villain;
import com.hgrranzi.swingum.view.gui.GuiFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.HashMap;
import java.util.Map;

import static com.hgrranzi.swingum.view.gui.ImageManager.getImage;
import static com.hgrranzi.swingum.view.gui.ImageManager.scaleImage;

public class GameView extends BaseView {

    private final Hero hero;

    private int level = 0;

    private int squareSize;

    private final Map<String, Image> images = new HashMap<>();

    private final JPanel inventoryPanel;

    private final JPanel navigationButtonsPanel;

    private final JPanel choiceButtonsPanel;

    private final JLabel statusLabel;

    public GameView(GameController gameController, Hero hero) {
        super(gameController);
        this.hero = hero;

        fetchGameLevel();

        addButton("Save game", e -> gameController.saveGame());
        addButton("Main menu", e -> this.gameController.switchView("WelcomeView"));

        westPanel.add(new JLabel(new ImageIcon(getImage(hero.getClazz().getImageName()))));
        westPanel.add(new JLabel(hero.getName() + " | LEVEL: " + level + " | XP: " + hero.getExperience(),
                JLabel.CENTER));
        inventoryPanel = createInventoryPanel();
        westPanel.add(inventoryPanel);
        westPanel.add(new JLabel(hero.getInfo(), JLabel.CENTER));

        navigationButtonsPanel = createNavigationButtonsPanel();
        choiceButtonsPanel = createChoiceButtonsPanel();
        statusLabel = createStatusLabel(hero.getStatus());

        eastPanel.add(new JLabel());
        eastPanel.add(choiceButtonsPanel).setVisible(false);
        eastPanel.add(navigationButtonsPanel);
        eastPanel.add(statusLabel);
    }

    private JPanel createInventoryPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        for (ArtefactType artefact : ArtefactType.values()) {
            JLabel artefactIcon = new JLabel(new ImageIcon(scaleImage(getImage(artefact.getImageName()), squareSize, squareSize)));
            artefactIcon.setBorder(BorderFactory.createRaisedSoftBevelBorder());
            artefactIcon.setPreferredSize(new Dimension(squareSize, squareSize));
            artefactIcon.setEnabled(false);
            panel.add(artefactIcon);
        }
        return panel;
    }

    private JLabel createStatusLabel(String text) {
        JLabel feedbackLabel = new JLabel(text);
        feedbackLabel.setHorizontalAlignment(JLabel.CENTER);
        return feedbackLabel;
    }

    private void fetchStatus() {
        statusLabel.setText(hero.getStatus());
    }

    private void saveScaledImage(String imageName, int width, int height) {
        images.put(imageName, scaleImage(getImage(imageName), width, height));
    }

    private JPanel createNavigationButtonsPanel() {
        JPanel buttonPanel = new JPanel(new GridLayout(4, 5, 10, 10));

        for (int i = 0; i < 16; i++) {
            buttonPanel.add(new JLabel(""));
        }

        JButton buttonUp = new JButton();
        JButton buttonLeft = new JButton();
        JButton buttonRight = new JButton();
        JButton buttonDown = new JButton();

        buttonUp.addActionListener(e -> gameController.moveHero('n'));
        buttonLeft.addActionListener(e -> gameController.moveHero('w'));
        buttonRight.addActionListener(e -> gameController.moveHero('e'));
        buttonDown.addActionListener(e -> gameController.moveHero('s'));

        buttonPanel.add(buttonUp, 7);
        buttonPanel.add(buttonLeft, 11);
        buttonPanel.add(buttonRight, 13);
        buttonPanel.add(buttonDown, 17);

        buttonPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateButtonIcons(buttonPanel);
            }
        });

        return buttonPanel;
    }

    private void updateButtonIcons(JPanel panel) {
        JButton exampleButton = (JButton) panel.getComponent(7);
        int size = Math.min(exampleButton.getWidth(), exampleButton.getHeight());
        setButtonIcon((JButton) panel.getComponent(7), "up.png", size);
        setButtonIcon((JButton) panel.getComponent(11), "left.png", size);
        setButtonIcon((JButton) panel.getComponent(13), "right.png", size);
        setButtonIcon((JButton) panel.getComponent(17), "down.png", size);
    }

    private void setButtonIcon(JButton button, String iconName, int size) {
        ImageIcon icon = new ImageIcon(scaleImage(getImage(iconName), size, size));
        button.setIcon(icon);
    }


    private JPanel createChoiceButtonsPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton acceptButton = new JButton("");
        acceptButton.addActionListener(e -> gameController.processAcceptInteraction());
        buttonPanel.add(acceptButton);


        JButton refuseButton = new JButton("");
        refuseButton.addActionListener(e -> gameController.processRefuseInteraction());
        buttonPanel.add(refuseButton);

        return buttonPanel;
    }

    @Override
    public void refresh() {
        fetchGameLevel();
        fetchHeroInfo();
        updateInteractionUI();
        revalidate();
        repaint();
    }

    private void fetchHeroInfo() {
        ((JLabel)westPanel.getComponent(1)).setText(hero.getName() + " | LEVEL: " + level + " | XP: " + hero.getExperience());
        for (Artefact artefact : hero.getInventory()) {
            if (artefact != null) {
                inventoryPanel.getComponent(artefact.getType().ordinal()).setEnabled(true);
            }
        }
        ((JLabel)westPanel.getComponent(3)).setText(hero.getInfo());
    }

    private void updateInteractionUI() {
        fetchStatus();
        if (hero.getInteraction() == null) {
            enableNavigationButtons(true);
            enableChoiceButtons(false);
            enableInteractionImage(false);
        } else {
            enableNavigationButtons(false);
            enableChoiceButtons(true);
            enableInteractionImage(true);
        }
    }

    private void enableNavigationButtons(boolean enable) {
        navigationButtonsPanel.getComponent(7).setEnabled(enable);
        navigationButtonsPanel.getComponent(11).setEnabled(enable);
        navigationButtonsPanel.getComponent(13).setEnabled(enable);
        navigationButtonsPanel.getComponent(17).setEnabled(enable);
    }

    private void enableChoiceButtons(boolean enable) {
        if (enable) {
            JButton acceptButton = (JButton) choiceButtonsPanel.getComponent(0);
            acceptButton.setText(hero.getInteraction().getOptions().get(0));
            if (hero.getInteraction().getOptions().size() > 1) {
                JButton refuseButton = (JButton) choiceButtonsPanel.getComponent(1);
                refuseButton.setText(hero.getInteraction().getOptions().get(1));
                refuseButton.setVisible(true);
            } else {
                choiceButtonsPanel.getComponent(1).setVisible(false);
            }
            choiceButtonsPanel.setVisible(true);
        } else {
            choiceButtonsPanel.setVisible(false);
        }
    }

    private void enableInteractionImage(boolean enable) {
        eastPanel.remove(0);
        if (enable) {
            eastPanel.add(new JLabel(new ImageIcon(getImage(hero.getInteraction().getImageName()))), 0);
        } else {
            eastPanel.add(new JLabel(""), 0);
        }
    }

    private void fetchGameLevel() {
        if (level == hero.getLevel() && !images.isEmpty()) {
            return;
        }
        int mapWidth = Math.min(GuiFrame.getFrameWidth() - westPanel.getPreferredSize().width * 2,
                GuiFrame.getFrameHeight() - northPanel.getPreferredSize().height * 2) - 20;
        this.squareSize = mapWidth / hero.getGameLevel().getMapSize();
        images.clear();
        loadAndScaleImages();
        level = hero.getLevel();
    }

    private void loadAndScaleImages() {
        saveScaledImage(hero.getClazz().getImageName(), squareSize - 1, squareSize - 1);
        for (Villain villain : hero.getGameLevel().getVillains()) {
            saveScaledImage(villain.getType().getImageName(), squareSize - 1, squareSize - 1);
            if (villain.getArtefact() != null) {
                saveScaledImage(villain.getArtefact().getType().getImageName(), squareSize - 1, squareSize - 1);
            }
        }
    }


    public void drawCenterPanel(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        drawMap(g2);
        drawVillains(g2);
        drawHero(g2);
    }

    private void drawMap(Graphics2D g2) {
        for (int i = 0; i < hero.getGameLevel().getMapSize(); ++i) {
            for (int j = 0; j < hero.getGameLevel().getMapSize(); ++j) {
                g2.setColor(Color.DARK_GRAY);
                g2.fillRect((squareSize * i), squareSize * j, squareSize - 1, squareSize - 1);
            }
        }
    }

    private void drawVillains(Graphics2D g2) {
        for (Villain villain : hero.getGameLevel().getVillains()) {
            g2.drawImage(images.get(villain.getType().getImageName()),
                    villain.getPosX() * squareSize,
                    villain.getPosY() * squareSize,
                    this);
        }
    }

    private void drawHero(Graphics2D g2) {
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRect((squareSize * hero.getGameLevel().getHeroX()),
                squareSize * hero.getGameLevel().getHeroY(),
                squareSize - 1,
                squareSize - 1);
        g2.drawImage(images.get(hero.getClazz().getImageName()),
                hero.getGameLevel().getHeroX() * squareSize,
                hero.getGameLevel().getHeroY() * squareSize,
                this);
    }

}

