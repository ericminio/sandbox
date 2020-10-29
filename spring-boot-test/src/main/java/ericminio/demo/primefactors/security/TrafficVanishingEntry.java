package ericminio.demo.primefactors.security;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TrafficVanishingEntry implements Runnable {

    private Object key;
    private long inactivityDelay;
    private TimeUnit unit;
    TrafficLimiter trafficLimiter;
    private SemaphoreWithReplenishment semaphore;
    private ScheduledExecutorService executor;
    private long lastAccessTime;

    public TrafficVanishingEntry(Object key, TrafficLimiter trafficLimiter) {
        this.key = key;
        this.trafficLimiter = trafficLimiter;
        TrafficLimiterConfiguration configuration = trafficLimiter.getConfiguration();
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
        this.trafficLimiter.remove(this);
        this.semaphore.stop();
        this.executor.shutdown();
    }

    public Object getKey() {
        return key;
    }
}
