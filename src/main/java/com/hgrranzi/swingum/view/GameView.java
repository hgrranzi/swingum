package com.hgrranzi.swingum.view;

import com.hgrranzi.swingum.controller.GameController;
import com.hgrranzi.swingum.model.Hero;
import com.hgrranzi.swingum.model.Villain;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import static com.hgrranzi.swingum.view.gui.ImageManager.getImage;

public class GameView extends BaseView {

    private Hero hero;

    private int squareSize = 50;

    private static final Map<String, Image> images = new HashMap<>();

    public GameView(GameController gameController, Hero hero) {
        super(gameController);
        this.hero = hero;
        this.squareSize = countSquareSize(this.hero.getGameLevel().getMapSize());

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

        addButton("Up", e -> gameController.moveHero('n'));
        addButton("Down", e -> gameController.moveHero('s'));
        addButton("Left", e -> gameController.moveHero('w'));
        addButton("Right", e -> gameController.moveHero('e'));
    }

    private int countSquareSize(int mapSize) {
        // todo: implement this method
        return 50;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;

        this.drawMap(g2);
        drawVillains(g2);
        this.drawHero(g2);
    }

    private void drawMap(Graphics2D g2) {
        setPreferredSize(new Dimension(squareSize * hero.getGameLevel().getMapSize(),
                                       squareSize * hero.getGameLevel().getMapSize()));

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

    private void drawVillains(Graphics2D g2) {
        for (Villain villain : hero.getGameLevel().getVillains()) {
            g2.drawImage(images.get(villain.getType().getImageName()),
                         villain.getPosX() * squareSize,
                         villain.getPosY() * squareSize,
                         this);
        }
    }

    private void drawHero(Graphics2D g2) {
        g2.drawImage(images.get(hero.getClazz().getImageName()),
                     hero.getGameLevel().getHeroX() * squareSize,
                     hero.getGameLevel().getHeroY() * squareSize,
                     this);
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

}

