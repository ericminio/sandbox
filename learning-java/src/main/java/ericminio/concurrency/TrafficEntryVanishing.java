package ericminio.concurrency;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TrafficEntryVanishing implements Runnable {

    private Object key;
    private long inactivityDelay;
    private TimeUnit unit;
    TrafficLimiterUsing2nThreads trafficLimiterUsing2nThreads;
    private SemaphoreWithReplenishment semaphore;
    private ScheduledExecutorService executor;
    private long lastAccessTime;

    public TrafficEntryVanishing(Object key, TrafficLimiterUsing2nThreads trafficLimiterUsing2nThreads) {
        this.key = key;
        this.trafficLimiterUsing2nThreads = trafficLimiterUsing2nThreads;
        TrafficLimiterConfiguration configuration = trafficLimiterUsing2nThreads.getConfiguration();
        this.inactivityDelay = configuration.getInactivityDelay();
        this.unit = configuration.getUnit();
        this.semaphore = new SemaphoreWithReplenishment(configuration.getPermits(), configuration.getDelay(), configuration.getUnit());
        this.executor = Executors.newScheduledThreadPool(1);
        this.executor.scheduleWithFixedDelay(this, configuration.getInactivityDelay(), configuration.getDelay(), configuration.getUnit());
    }

    public boolean isStillVisible() {
        this.lastAccessTime = System.currentTimeMillis();
        return semaphore.tryAcquire();
    }

    @Override
    public void run() {
        long delay = this.unit.convert(System.currentTimeMillis() - this.lastAccessTime, TimeUnit.MILLISECONDS);
        if (delay > this.inactivityDelay) {
            clean();
        }
    }

    public void clean() {
        this.trafficLimiterUsing2nThreads.remove(this);
        this.semaphore.stop();
        this.executor.shutdown();
    }

    public Object getKey() {
        return key;
    }
}
