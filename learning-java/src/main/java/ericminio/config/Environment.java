package ericminio.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Environment {

    public String valueOf(String key) throws IOException {
        String env = System.getProperty("environment");
        Properties properties = new Properties();
        InputStream stream = this.getClass().getClassLoader().getResourceAsStream("application-"+env+".properties");
        properties.load(stream);

        return properties.getProperty(key);
    }
}
