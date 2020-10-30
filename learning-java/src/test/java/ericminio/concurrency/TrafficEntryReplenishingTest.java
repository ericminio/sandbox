package ericminio.concurrency;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class TrafficEntryReplenishingTest {

    @Test
    public void works() throws InterruptedException {
        TrafficLimiterConfiguration configuration = new TrafficLimiterConfiguration();
        configuration.setPermits(3);
        configuration.setDelay(150);
        configuration.setUnit(TimeUnit.MILLISECONDS);
        TrafficEntryReplenishing semaphore = new TrafficEntryReplenishing("any", configuration);
        assertThat(semaphore.tryAcquire(), equalTo(true));
        assertThat(semaphore.tryAcquire(), equalTo(true));
        assertThat(semaphore.tryAcquire(), equalTo(true));
        assertThat(semaphore.availablePermits(), equalTo(0));
        Thread.sleep(200);
        assertThat(semaphore.availablePermits(), equalTo(3));
        assertThat(semaphore.tryAcquire(), equalTo(true));
        assertThat(semaphore.tryAcquire(), equalTo(true));
        assertThat(semaphore.tryAcquire(), equalTo(true));
        assertThat(semaphore.availablePermits(), equalTo(0));
        Thread.sleep(200);
        assertThat(semaphore.availablePermits(), equalTo(3));
    }

    @Test
    public void createsOneThreadPerSemaphore() {
        ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
        int initialCount = threadGroup.activeCount();
        TrafficLimiterConfiguration configuration = new TrafficLimiterConfiguration();
        configuration.setPermits(3);
        configuration.setDelay(150);
        configuration.setUnit(TimeUnit.MILLISECONDS);

        new TrafficEntryReplenishing("any", configuration);
        assertThat(threadGroup.activeCount(), equalTo(initialCount + 1));
        new TrafficEntryReplenishing("any", configuration);
        assertThat(threadGroup.activeCount(), equalTo(initialCount + 2));
        new TrafficEntryReplenishing("any", configuration);
        assertThat(threadGroup.activeCount(), equalTo(initialCount + 3));
    }
    @Test
    public void offersOneWayToStopTheThread() throws InterruptedException {
        ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
        int initialCount = threadGroup.activeCount();
        TrafficLimiterConfiguration configuration = new TrafficLimiterConfiguration();
        configuration.setPermits(3);
        configuration.setDelay(150);
        configuration.setUnit(TimeUnit.MILLISECONDS);
        TrafficEntryReplenishing semaphore = new TrafficEntryReplenishing("any", configuration);
        semaphore.stopReplenishing();
        Thread.sleep(300);

        assertThat(threadGroup.activeCount(), equalTo(initialCount));
    }
}
