package com.hgrranzi.swingum.view;

import com.hgrranzi.swingum.controller.GameController;
import com.hgrranzi.swingum.model.GameLevel;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class GameView extends BaseView {

    private GameLevel gameLevel = new GameLevel(1);
    private int squareSize = 50;

    public GameView(GameController controller, GameLevel gameLevel) {
        super(controller);
        // this.gameLevel = gameLevel;
        squareSize = countSquareSize(this.gameLevel.getMapSize());
        addButton("Save game", e -> System.out.println("Game saved"));
        addButton("Load game", e -> controller.showTheView("LoadGameView"));
        addButton("Main menu", e -> controller.showTheView("WelcomeView"));
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
        setPreferredSize(new Dimension(squareSize * gameLevel.getMapSize(), squareSize * gameLevel.getMapSize()));

        for (int i = 0; i < gameLevel.getMapSize(); ++i) {
            for (int j = 0; j < gameLevel.getMapSize(); ++j) {
                if (!gameLevel.isExploredArea(i, j)) {
                    g2.setColor(Color.DARK_GRAY);
                    g2.fillRect((squareSize * j), squareSize * i - 1, squareSize - 1, squareSize - 1);
                } else {
                    g2.setColor(Color.LIGHT_GRAY);
                    g2.fillRect((squareSize * j), squareSize * i - 1, squareSize - 1, squareSize - 1);
                }
            }
        }
    }

    private void drawHero(Graphics2D g2) {
        Image img = getToolkit().getImage("/mnt/nfs/homes/uteena/sgoinfre/proj/swingum/src/main/resources/go.png");

        prepareImage(img, this);

        img = scaleImage(img, squareSize - 1, squareSize - 1);

        g2.drawImage(img, gameLevel.getHeroX() * squareSize, gameLevel.getHeroY() * squareSize, this);

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

