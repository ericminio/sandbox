package ericminio.concurrency;

import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class SemaphoreTest {

    @Test
    public void canHelpLimitingAccessToOneResource() {
        boolean allowed = true;
        Semaphore semaphore = new Semaphore(3);
        allowed = semaphore.tryAcquire();
        assertThat(allowed, equalTo(true));
        allowed = semaphore.tryAcquire();
        assertThat(allowed, equalTo(true));
        allowed = semaphore.tryAcquire();
        assertThat(allowed, equalTo(true));
        allowed = semaphore.tryAcquire();
        assertThat(allowed, equalTo(false));
    }
    @Test
    public void isThreadSafe() throws Exception {
        Semaphore semaphore = new Semaphore(3);
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        IntStream.range(0, 3).forEach(attempt -> executorService.execute(semaphore::tryAcquire));
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.SECONDS);

        assertThat(semaphore.availablePermits(), equalTo(0));
    }
    @Test
    public void replenishmentIsNotNative() {
        Semaphore semaphore = new Semaphore(3);
        semaphore.tryAcquire();

        semaphore.drainPermits();
        assertThat(semaphore.availablePermits(), equalTo(0));

        semaphore.release(3);
        assertThat(semaphore.availablePermits(), equalTo(3));
    }
}
