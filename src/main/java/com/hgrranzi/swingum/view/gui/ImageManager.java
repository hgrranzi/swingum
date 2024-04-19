package com.hgrranzi.swingum.view.gui;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static com.hgrranzi.swingum.config.ApplicationConfig.getProperty;

public class ImageManager {

    public static final String IMAGES_PATH = "images.path";

    private static final Map<String, Image> imageCache = new HashMap<>();

    public static Image getImage(String imageName) {
        Image image = imageCache.get(imageName);
        if (image == null) {
            image = loadImage(imageName);
            imageCache.put(imageName, image);
        }
        return image;
    }

    private static Image loadImage(String imageName) {
        InputStream inputStream = ImageManager.class.getResourceAsStream(getProperty(IMAGES_PATH) + imageName);
        try {
            assert inputStream != null;
            return ImageIO.read(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load image: " + imageName, e);
        }
    }

    public static Image scaleImage(Image img, int targetWidth, int targetHeight) {
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

