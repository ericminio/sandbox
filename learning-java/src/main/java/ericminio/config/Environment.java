package ericminio.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Environment {

    private Properties properties;

    public String valueOf(String key) throws IOException {
        return read().getProperty(key);
    }

    protected Properties read() throws IOException {
        if (properties == null) {
            properties = new Properties();
            InputStream stream = this.getClass().getClassLoader().getResourceAsStream(
                    "application-"+ System.getProperty("environment") +".properties");
            properties.load(stream);
        }
        return properties;
    }
}
