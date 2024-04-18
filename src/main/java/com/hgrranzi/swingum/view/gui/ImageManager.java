package com.hgrranzi.swingum.view.gui;

import javax.imageio.ImageIO;
import java.awt.*;
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
}

