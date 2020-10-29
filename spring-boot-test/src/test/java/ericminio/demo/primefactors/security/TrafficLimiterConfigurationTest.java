package ericminio.demo.primefactors.security;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TrafficLimiterConfigurationTest {

    @Autowired
    TrafficLimiterConfiguration configuration;

    @Test
    public void providesPermits() {
        assertThat(configuration.getPermits(), equalTo(1));
    }

    @Test
    public void providesDelay() {
        assertThat(configuration.getDelay(), equalTo(2L));
    }

    @Test
    public void providesInactivityDelay() {
        assertThat(configuration.getInactivityDelay(), equalTo(10L));
    }

    @Test
    public void providesUnit() {
        assertThat(configuration.getUnit(), equalTo(TimeUnit.SECONDS));
    }
}
