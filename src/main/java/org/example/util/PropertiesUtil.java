package org.example.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesUtil {
    public static String getBaseUrlValue(String key) throws IOException {
        Properties prop = new Properties();
        FileInputStream fis = new FileInputStream("src/test/resources/application.properties");
        prop.load(fis);
        return prop.getProperty(key);
    }
}
