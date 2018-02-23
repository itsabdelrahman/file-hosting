package Utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.UUID;

public class Main {
    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }

    public static Properties readPropertiesFile(String filePath) throws IOException {
        Properties properties = new Properties();
        FileInputStream fileInputStream = new FileInputStream(filePath);

        properties.load(fileInputStream);
        fileInputStream.close();

        return properties;
    }
}
