package ericminio.concurrency;

import java.util.concurrent.*;

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
