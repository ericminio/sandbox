package ericminio.concurrency;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class TrafficLimiterUsing2nThreadsTest extends TrafficLimiterTest {

    @Override
    protected TrafficLimiter getTrafficLimiter(TrafficLimiterConfiguration configuration) {
        return new TrafficLimiterUsing2nThreads(configuration);
    }

    @Test
    public void cleanupStopsAssociatedThreads() throws InterruptedException {
        ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
        int initialCount = threadGroup.activeCount();
        trafficLimiter.isOpen(3);
        assertThat(threadGroup.activeCount(), equalTo(initialCount + 2));

        Thread.sleep(450 + 200);
        assertThat(threadGroup.activeCount(), equalTo(initialCount));
    }
}
