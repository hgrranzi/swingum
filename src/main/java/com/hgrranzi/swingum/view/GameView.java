package com.hgrranzi.swingum.view;

import com.hgrranzi.swingum.controller.GameController;
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

    private Hero hero;

    private int squareSize;

    private final Map<String, Image> images = new HashMap<>();

    private final JPanel navigationButtonsPanel;

    private final JPanel choiceButtonsPanel;

    public GameView(GameController gameController, Hero hero) {
        super(gameController);
        this.hero = hero;
        int mapWidth = Math.min(GuiFrame.getFrameWidth() - westPanel.getPreferredSize().width * 2,
                                GuiFrame.getFrameHeight() - northPanel.getPreferredSize().height * 2) - 30;
        this.squareSize = mapWidth / hero.getGameLevel().getMapSize();

        saveScaledImage(hero.getClazz().getImageName(), squareSize - 1, squareSize - 1);
        for (Villain villain : hero.getGameLevel().getVillains()) {
            saveScaledImage(villain.getType().getImageName(), squareSize - 1, squareSize - 1);
            if (villain.getArtefact() != null) {
                saveScaledImage(villain.getArtefact().getType().getImageName(), squareSize - 1, squareSize - 1);
            }
        }

        addButton("Save game", e -> gameController.saveGame());
        addButton("Main menu", e -> this.gameController.switchView("WelcomeView"));

        navigationButtonsPanel = createNavigationButtonsPanel();
        choiceButtonsPanel = createChoiceButtonsPanel();

        eastPanel.add(new JLabel(""));
        eastPanel.add(navigationButtonsPanel);
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
        JPanel buttonPanel = new JPanel(new GridLayout(4, 4, 10, 10));

        for (int i = 0; i < 14; i++) {
            buttonPanel.add(new JLabel(""));
        }

        JButton buttonYes = new JButton("");

        buttonPanel.add(buttonYes, 9);

        JButton buttonNo = new JButton("");

        buttonPanel.add(buttonNo, 10);

        return buttonPanel;
    }

    @Override
    public void refresh() {
        // todo: update panels: center or east or west depending on what happens
        if (hero.getInteractions().isEmpty()) {
            centerPanel.revalidate();
            centerPanel.repaint();
        } else {
            eastPanel.removeAll();
            choiceButtonsPanel.remove(9);
            choiceButtonsPanel.remove(9);
            System.out.println(hero.getInteractions().get(0).getInteractions());
            int i = 9;
            for (String option : hero.getInteractions().get(0).getInteractions()) {
                System.out.println(option);
                JButton button = new JButton(option);
                button.addActionListener(e -> gameController.processInteraction(option));
                choiceButtonsPanel.add(button, i);
                i++;
            }
            eastPanel.add(new JLabel(new ImageIcon(getImage(hero.getInteractions().get(0).getImageName()))));
            eastPanel.add(choiceButtonsPanel);
            eastPanel.revalidate();
            eastPanel.repaint();
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
        g2.setColor(Color.decode("#cda4de"));
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

