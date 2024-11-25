package ru.netology.chat.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Settings {
    private static final Logger logger = LoggerFactory.getLogger(Settings.class);
    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 8989;

    private final Properties properties;

    public Settings(String configPath) {
        properties = new Properties();
        try (FileInputStream fis = new FileInputStream(configPath)) {
            properties.load(fis);
            logger.info("Loaded settings from: {}", configPath);
        } catch (IOException e) {
            logger.warn("Could not load settings file, using defaults: ", e);
        }
    }

    public String getServerHost() {
        return properties.getProperty("server.host", DEFAULT_HOST);
    }

    public int getServerPort() {
        try {
            return Integer.parseInt(properties.getProperty("server.port", String.valueOf(DEFAULT_PORT)));
        } catch (NumberFormatException e) {
            logger.warn("Invalid port in settings, using default: ", e);
            return DEFAULT_PORT;
        }
    }
}
