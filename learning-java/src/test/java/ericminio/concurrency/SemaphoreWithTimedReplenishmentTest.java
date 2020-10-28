package ericminio.concurrency;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class SemaphoreWithTimedReplenishmentTest {

    @Test
    public void works() throws InterruptedException {
        SemaphoreWithTimedReplenishment semaphore =
                new SemaphoreWithTimedReplenishment(3, 150, TimeUnit.MILLISECONDS);
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
}
