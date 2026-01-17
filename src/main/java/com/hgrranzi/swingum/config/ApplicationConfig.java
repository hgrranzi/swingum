package com.hgrranzi.swingum.config;

import java.io.InputStream;
import java.util.Properties;
import java.util.Random;

public final class ApplicationConfig {

    private ApplicationConfig() {
        // Utility class - prevent instantiation
    }

    private static final Properties PROPERTIES = new Properties();

    private static final Random RANDOM = new Random();

    static {
        loadProperties();
    }

    public static void loadProperties() {
        try (InputStream is = ApplicationConfig.class.getClassLoader().getResourceAsStream("application.properties")) {
            PROPERTIES.load(is);
        } catch (Exception e) {
            throw new RuntimeException("Error loading application properties", e);
        }
    }

    public static String getProperty(String key) {
        return PROPERTIES.getProperty(key);
    }

    public static Random getRandom() {
        return RANDOM;
    }
}
