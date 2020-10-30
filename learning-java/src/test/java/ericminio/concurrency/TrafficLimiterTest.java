package ericminio.concurrency;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public abstract class TrafficLimiterTest {

    TrafficLimiter trafficLimiter;

    @Before
    public void sut() {
        TrafficLimiterConfiguration configuration = new TrafficLimiterConfiguration();
        configuration.setPermits(1);
        configuration.setDelay(150);
        configuration.setInactivityDelay(450);
        configuration.setUnit(TimeUnit.MILLISECONDS);

        trafficLimiter = getTrafficLimiter(configuration);
    }
    @After
    public void clean() throws InterruptedException {
        trafficLimiter.stop();
        Thread.sleep(150);
    }

    protected abstract TrafficLimiter getTrafficLimiter(TrafficLimiterConfiguration configuration);

    @Test
    public void doesNotLimitsRateWhenInputIsDifferent() {
        assertThat(trafficLimiter.isOpen(3), equalTo(false));
        assertThat(trafficLimiter.isOpen(5), equalTo(false));
        assertThat(trafficLimiter.isOpen(7), equalTo(false));
    }

    @Test
    public void limitsRateForSameInput() throws InterruptedException {
        assertThat(trafficLimiter.isOpen(15), equalTo(false));
        assertThat(trafficLimiter.isOpen(15), equalTo(true));

        Thread.sleep(150 + 50);
        assertThat(trafficLimiter.isOpen(15), equalTo(false));
    }

    @Test
    public void cleanupAfterInactivityDelay() throws InterruptedException {
        trafficLimiter.isOpen(3);
        assertThat(trafficLimiter.trafficSize(), equalTo(1));

        Thread.sleep(450 + 200);
        assertThat(trafficLimiter.trafficSize(), equalTo(0));
    }

    @Test
    public void doesNotCleanAsLongAsActivityExists() throws InterruptedException {
        trafficLimiter.isOpen(3);
        Thread.sleep(300);
        trafficLimiter.isOpen(3);
        Thread.sleep(150 + 100);

        assertThat(trafficLimiter.trafficSize(), equalTo(1));
    }
}
