package de.kneitzel;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TestBase {

    protected Properties prop = new Properties();

    public TestBase() {

        // Load the properties from the application properties.
        ClassLoader loader = getClass().getClassLoader();
        try (InputStream in = loader.getResourceAsStream("application.properties")) {
            prop.load(in);
        } catch (IOException ex) {
            throw new RuntimeException("Error loading the application.properties file.", ex);
        }
    }
}
