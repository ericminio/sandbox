package ericminio.concurrency;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class SemaphoreWithReplenishmentTest {

    @Test
    public void works() throws InterruptedException {
        SemaphoreWithReplenishment semaphore =
                new SemaphoreWithReplenishment(3, 150, TimeUnit.MILLISECONDS);
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

        new SemaphoreWithReplenishment(3, 150, TimeUnit.MILLISECONDS);
        assertThat(threadGroup.activeCount(), equalTo(initialCount + 1));
        new SemaphoreWithReplenishment(3, 150, TimeUnit.MILLISECONDS);
        assertThat(threadGroup.activeCount(), equalTo(initialCount + 2));
        new SemaphoreWithReplenishment(3, 150, TimeUnit.MILLISECONDS);
        assertThat(threadGroup.activeCount(), equalTo(initialCount + 3));
    }
    @Test
    public void offersOneWayToStopTheThread() throws InterruptedException {
        ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
        int initialCount = threadGroup.activeCount();
        SemaphoreWithReplenishment semaphore = new SemaphoreWithReplenishment(3, 150, TimeUnit.MILLISECONDS);
        semaphore.stop();
        Thread.sleep(100);

        assertThat(threadGroup.activeCount(), equalTo(initialCount));
    }
}
