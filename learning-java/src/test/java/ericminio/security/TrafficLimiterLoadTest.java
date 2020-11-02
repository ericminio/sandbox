package ericminio.security;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.*;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class TrafficLimiterLoadTest {

    TrafficLimiter trafficLimiter;
    int threadCount = 100;
    int inactivityDelay = 450;

    @Before
    public void sut() {
        TrafficLimiterConfiguration configuration = new TrafficLimiterConfiguration();
        configuration.setPermits(1);
        configuration.setDelay(150);
        configuration.setInactivityDelay(inactivityDelay);
        configuration.setUnit(TimeUnit.MILLISECONDS);

        trafficLimiter = new TrafficLimiterUsing1Thread(configuration);
    }
    @After
    public void clean() throws InterruptedException {
        trafficLimiter.stop();
        Thread.sleep(150);
    }
    class AccessCounter {
        private int value = 0;
        public synchronized void increment() { this.value += 1; }
        public int getValue() { return this.value; }
    }

    @Test
    public void exploration() throws Exception {
        AccessCounter starterCounter = new AccessCounter();
        AccessCounter successCounter = new AccessCounter();
        AccessCounter blockCounter = new AccessCounter();
        ScheduledFuture<?> startYourEngine = Executors.newScheduledThreadPool(1).schedule(() -> {
            while (starterCounter.getValue() != threadCount) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    fail(e.getMessage());
                }
            }
            debug("go!");
        }, 1, TimeUnit.SECONDS);
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(threadCount);
        int successfulAccessesPerThread = 3;
        for (int i = 0; i< threadCount; i++) {
            final int threadIndex = i;
            final Integer number = new Integer(i % 10);
            executorService.schedule(() -> {
                try {
                    debug(threadIndex + ", " + number + ": " + Thread.currentThread().hashCode() + " : waiting for starter. " + System.currentTimeMillis());
                    starterCounter.increment();
                    startYourEngine.get();
                    debug(number + ": got it. " + System.currentTimeMillis());
                    for (int j=0; j<successfulAccessesPerThread; j++) {
                        while (!trafficLimiter.isOpen(number)) {
                            blockCounter.increment();
                            Thread.sleep(inactivityDelay + 50);
                        }
                        successCounter.increment();
                    }
                    debug(number + ": done. " + System.currentTimeMillis());
                } catch (InterruptedException e) {
                    fail(e.getMessage());
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }, 1, TimeUnit.SECONDS);
        }
        executorService.shutdown();
        boolean termination = executorService.awaitTermination(60, TimeUnit.SECONDS);

        assertThat(termination, equalTo(true));
        assertThat(successCounter.getValue(), equalTo(successfulAccessesPerThread * threadCount));
        assertThat(blockCounter.getValue(), greaterThan(threadCount));

        debug("Block count = " + blockCounter.getValue());
    }

    private void debug(String message) {
        //System.out.println(message);
    }
}
