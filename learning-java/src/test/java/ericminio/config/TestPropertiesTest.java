package ericminio.config;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class TestPropertiesTest extends TestEnvironment {

    Environment environment;

    @Before
    public void usingEnvironment() {
        environment = new Environment();
    }

    @Test
    public void makesPropertiesVisible() throws IOException {
        assertThat(environment.valueOf("target"), equalTo("testing"));
    }
}
