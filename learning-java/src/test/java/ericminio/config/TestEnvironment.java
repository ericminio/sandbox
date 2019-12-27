package ericminio.config;

import org.junit.Before;

public class TestEnvironment {

    @Before
    public void forceTest() {
        System.setProperty("environment", "test");
    }
}
