package com.hgrranzi.swingum.view.gui;

import com.hgrranzi.swingum.controller.GameController;
import com.hgrranzi.swingum.model.Hero;
import com.hgrranzi.swingum.model.HeroClass;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class GameView extends BaseView {

    private Hero hero = new Hero("Hero", HeroClass.CLASS1);
    private int squareSize = 50;

    public GameView(GameController controller) {
        super(controller);
        // this.hero.getGameLevel() = hero.getGameLevel();
        squareSize = countSquareSize(this.hero.getGameLevel().getMapSize());
        addButton("Save game", e -> System.out.println("Game saved"));
        addButton("Load game", e -> controller.switchView("LoadGameView"));
        addButton("Main menu", e -> controller.switchView("WelcomeView"));
        addButton("Up", new ArrowButtonListener('n'));
        addButton("Down", new ArrowButtonListener('s'));
        addButton("Left", new ArrowButtonListener('w'));
        addButton("Right", new ArrowButtonListener('e'));
    }

    private int countSquareSize(int mapSize) {
        return 50;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;

        this.drawMap(g2);
        this.drawHero(g2);
    }

    private void drawMap(Graphics2D g2) {
        setPreferredSize(new Dimension(squareSize * hero.getGameLevel().getMapSize(), squareSize * hero.getGameLevel().getMapSize()));

        for (int i = 0; i < hero.getGameLevel().getMapSize(); ++i) {
            for (int j = 0; j < hero.getGameLevel().getMapSize(); ++j) {
                if (!hero.getGameLevel().isExploredArea(i, j)) {
                    g2.setColor(Color.DARK_GRAY);
                    g2.fillRect((squareSize * i), squareSize * j, squareSize - 1, squareSize - 1);
                } else {
                    g2.setColor(Color.LIGHT_GRAY);
                    g2.fillRect((squareSize * i), squareSize * j, squareSize - 1, squareSize - 1);
                }
            }
        }
    }

    private void drawHero(Graphics2D g2) {
        Image img = getToolkit().getImage("/mnt/nfs/homes/uteena/sgoinfre/proj/swingum/src/main/resources/go.png");

        prepareImage(img, this);

        img = scaleImage(img, squareSize - 1, squareSize - 1);

        g2.drawImage(img, hero.getGameLevel().getHeroX() * squareSize, hero.getGameLevel().getHeroY() * squareSize, this);

    }

    private Image scaleImage(Image img, int targetWidth, int targetHeight) {
        // Create a new BufferedImage with the target width and height
        BufferedImage scaledImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);

        // Get the graphics context of the scaled image
        Graphics2D g2d = scaledImage.createGraphics();

        // Apply scaling transformation
        double scaleX = (double) targetWidth / img.getWidth(null);
        double scaleY = (double) targetHeight / img.getHeight(null);
        AffineTransform transform = AffineTransform.getScaleInstance(scaleX, scaleY);
        g2d.drawImage(img, transform, null);

        // Dispose the graphics context
        g2d.dispose();

        // Return the scaled image
        return scaledImage;
    }

    private class ArrowButtonListener implements ActionListener {

        private final char direction;

        public ArrowButtonListener(char direction) {
            this.direction = direction;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            hero.move(direction);
            repaint();
        }
    }


}

