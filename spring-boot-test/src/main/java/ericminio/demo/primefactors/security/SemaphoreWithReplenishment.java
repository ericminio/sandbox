package ericminio.demo.primefactors.security;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class SemaphoreWithReplenishment extends Semaphore implements Runnable {

    private final ScheduledExecutorService scheduledExecutorService;
    private int permits;

    public SemaphoreWithReplenishment(int permits, long delay, TimeUnit unit) {
        super(permits);
        this.permits = permits;
        this.scheduledExecutorService = Executors.newScheduledThreadPool(1);
        this.scheduledExecutorService.scheduleWithFixedDelay(this, delay, delay, unit);
    }

    public void stop() {
        this.scheduledExecutorService.shutdown();
    }

    @Override
    public void run() {
        this.drainPermits();
        this.release(this.permits);
    }
}
