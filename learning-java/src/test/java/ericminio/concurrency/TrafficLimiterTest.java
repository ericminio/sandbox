package ericminio.concurrency;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class TrafficLimiterTest {

    TrafficLimiter trafficLimiter;

    @Before
    public void sut() {
        TrafficLimiterConfiguration configuration = new TrafficLimiterConfiguration();
        configuration.setPermits(1);
        configuration.setDelay(150);
        configuration.setInactivityDelay(450);
        configuration.setUnit(TimeUnit.MILLISECONDS);

        trafficLimiter = new TrafficLimiter();
        trafficLimiter.setConfiguration(configuration);
    }
    @After
    public void clean() throws InterruptedException {
        trafficLimiter.getTraffic().values().forEach(cleaner -> cleaner.clean());
        Thread.sleep(150);
    }

    @Test
    public void doesNotLimitsRateWhenInputIsDifferent() {
        assertThat(trafficLimiter.isLimitReachedFor(3), equalTo(false));
        assertThat(trafficLimiter.isLimitReachedFor(5), equalTo(false));
        assertThat(trafficLimiter.isLimitReachedFor(7), equalTo(false));
    }

    @Test
    public void limitsRateForSameInput() throws InterruptedException {
        assertThat(trafficLimiter.isLimitReachedFor(15), equalTo(false));
        assertThat(trafficLimiter.isLimitReachedFor(15), equalTo(true));

        Thread.sleep(200);
        assertThat(trafficLimiter.isLimitReachedFor(15), equalTo(false));
    }

    @Test
    public void cleanupAfterInactivityDelay() throws InterruptedException {
        trafficLimiter.isLimitReachedFor(3);
        assertThat(trafficLimiter.trafficSize(), equalTo(1));

        Thread.sleep(450 + 200);
        assertThat(trafficLimiter.trafficSize(), equalTo(0));
    }

    @Test
    public void cleanupStopsAssociatedThreads() throws InterruptedException {
        ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
        int initialCount = threadGroup.activeCount();
        trafficLimiter.isLimitReachedFor(3);
        assertThat(threadGroup.activeCount(), equalTo(initialCount + 2));

        Thread.sleep(450 + 200);
        assertThat(threadGroup.activeCount(), equalTo(initialCount));
    }

    @Test
    public void doesNotCleanAsLongAsActivityExists() throws InterruptedException {
        trafficLimiter.isLimitReachedFor(3);
        Thread.sleep(300);
        trafficLimiter.isLimitReachedFor(3);
        Thread.sleep(150 + 100);

        assertThat(trafficLimiter.trafficSize(), equalTo(1));
    }
}
