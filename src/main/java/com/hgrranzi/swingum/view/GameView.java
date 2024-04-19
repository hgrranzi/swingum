package com.hgrranzi.swingum.view;

import com.hgrranzi.swingum.controller.GameController;
import com.hgrranzi.swingum.model.Hero;
import com.hgrranzi.swingum.model.Villain;
import com.hgrranzi.swingum.view.gui.GuiFrame;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import static com.hgrranzi.swingum.view.gui.ImageManager.getImage;
import static com.hgrranzi.swingum.view.gui.ImageManager.scaleImage;

public class GameView extends BaseView {

    private Hero hero;

    private int squareSize;

    private int offsetX;

    private int offsetY;

    private final Map<String, Image> images = new HashMap<>();

    public GameView(GameController gameController, Hero hero) {
        super(gameController);
        this.hero = hero;
        int mapWidth = GuiFrame.getFrameWidth() / 2 - 20;
        this.squareSize = mapWidth / hero.getGameLevel().getMapSize();
        this.offsetX = (GuiFrame.getFrameWidth() - squareSize * hero.getGameLevel().getMapSize()) / 2;
        this.offsetY = (GuiFrame.getFrameHeight() - squareSize * hero.getGameLevel().getMapSize()) / 2;

        images.put(hero.getClazz().getImageName(),
                scaleImage(getImage(hero.getClazz().getImageName()), squareSize - 1, squareSize - 1));
        for (Villain villain : hero.getGameLevel().getVillains()) {
            images.put(villain.getType().getImageName(),
                    scaleImage(getImage(villain.getType().getImageName()), squareSize - 1, squareSize - 1));
            if (villain.getArtefact() != null) {
                images.put(villain.getArtefact().getType().getImageName(),
                        scaleImage(getImage(villain.getArtefact().getType().getImageName()), squareSize - 1,
                                squareSize - 1));
            }
        }

        addButton("Save game", e -> gameController.saveGame());
        addButton("Main menu", e -> this.gameController.switchView("WelcomeView"));
    }

    @Override
    public void displayGuiButtons() {
        super.displayGuiButtons();

        JPanel buttonPanel = new JPanel(new GridLayout(4, 5, 10, 10));
        buttonPanel.setPreferredSize(new Dimension(GuiFrame.getFrameHeight() / 6, GuiFrame.getFrameHeight() / 6));

        eastPanel.add(new JLabel(""));
        eastPanel.add(new JLabel(""));
        eastPanel.add(buttonPanel);

        JButton buttonU = new JButton(new ImageIcon(scaleImage(getImage("up.png"),
                GuiFrame.getFrameHeight() / 15,
                GuiFrame.getFrameHeight() / 15)));
        buttonU.addActionListener(e -> gameController.moveHero('n'));

        buttonPanel.add(new JLabel(""));
        buttonPanel.add(new JLabel(""));
        buttonPanel.add(new JLabel(""));
        buttonPanel.add(new JLabel(""));
        buttonPanel.add(new JLabel(""));
        buttonPanel.add(new JLabel(""));
        buttonPanel.add(new JLabel(""));
        buttonPanel.add(buttonU);
        buttonPanel.add(new JLabel(""));
        buttonPanel.add(new JLabel(""));
        buttonPanel.add(new JLabel(""));

        JButton buttonL = new JButton(new ImageIcon(scaleImage(getImage("left.png"),
                GuiFrame.getFrameHeight() / 15,
                GuiFrame.getFrameHeight() / 15)));
        buttonL.addActionListener(e -> gameController.moveHero('w'));

        buttonPanel.add(buttonL);

        JButton buttonD = new JButton(new ImageIcon(scaleImage(getImage("down.png"),
                GuiFrame.getFrameHeight() / 15,
                GuiFrame.getFrameHeight() / 15)));
        buttonD.addActionListener(e -> gameController.moveHero('s'));

        buttonPanel.add(buttonD);

        JButton buttonR = new JButton(new ImageIcon(scaleImage(getImage("right  .png"),
                GuiFrame.getFrameHeight() / 15,
                GuiFrame.getFrameHeight() / 15)));
        buttonR.addActionListener(e -> gameController.moveHero('e'));

        buttonPanel.add(buttonR);
        buttonPanel.add(new JLabel(""));
        buttonPanel.add(new JLabel(""));
        buttonPanel.add(new JLabel(""));
        buttonPanel.add(new JLabel(""));
        buttonPanel.add(new JLabel(""));
        buttonPanel.add(new JLabel(""));

    }



    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.translate(offsetX, offsetY);

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

