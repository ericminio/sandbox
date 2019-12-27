package ericminio.config;

import org.junit.Before;

public class ProductionEnvironment {

    @Before
    public void forceProd() {
        System.setProperty("environment", "prod");
    }
}
