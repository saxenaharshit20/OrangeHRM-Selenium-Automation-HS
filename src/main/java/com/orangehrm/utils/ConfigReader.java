package com.orangehrm.utils;

import com.orangehrm.constants.FrameworkConstants;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public final class ConfigReader {

    private static final Properties PROPERTIES = new Properties();

    static {
        try (FileInputStream input =
                     new FileInputStream(FrameworkConstants.CONFIG_FILE)) {
            PROPERTIES.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Unable to load config.properties", e);
        }
    }

    private ConfigReader() {}

    public static String get(String key) {
        return PROPERTIES.getProperty(key);
    }
}
