package ericminio.config;

import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class EnvironmentValueInjectionTest extends TestEnvironment {

    @Value(key="target")
    private String target;

    @Test
    public void requiresPlumbing() throws IOException {
        assertThat(target, equalTo("testing"));
    }
}
