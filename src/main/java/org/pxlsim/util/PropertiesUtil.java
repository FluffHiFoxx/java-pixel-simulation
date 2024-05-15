package org.pxlsim.util;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Slf4j
public class PropertiesUtil {

    private static final String configFileName = "config.properties";
    private static final ClassLoader loader = Thread.currentThread().getContextClassLoader();
    private static final Properties configProperties = new Properties();

    public static String getVersion() {
        loadConfigProperties();
        return configProperties.getProperty("version", "unknown");
    }

    public static int getWidth() {
        loadConfigProperties();
        return Integer.parseInt(configProperties.getProperty("width", "0"));
    }

    public static int getHeight() {
        loadConfigProperties();
        return Integer.parseInt(configProperties.getProperty("height", "0"));
    }

    public static int getZoom() {
        loadConfigProperties();
        return Integer.parseInt(configProperties.getProperty("zoom", "0"));
    }

    private static void loadConfigProperties() {
        if (configProperties.isEmpty()) {
            try {
                InputStream resourceStream = loader.getResourceAsStream(configFileName);
                configProperties.load(resourceStream);
            } catch (IOException io) {
                log.error(io.getMessage());
            }
        }
    }
}
