package ericminio.security;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TrafficEntryVanishing extends TrafficEntryReplenishing {

    private long inactivityDelay;
    private TimeUnit unit;
    TrafficLimiter trafficLimiter;
    private ScheduledExecutorService executor;

    public TrafficEntryVanishing(Object key, TrafficLimiterConfiguration configuration, TrafficLimiter trafficLimiter) {
        super(key, configuration);
        this.trafficLimiter = trafficLimiter;
        this.inactivityDelay = configuration.getInactivityDelay();
        this.unit = configuration.getUnit();
        this.executor = Executors.newScheduledThreadPool(1);
        this.executor.scheduleWithFixedDelay(() -> vanish(), configuration.getInactivityDelay(), configuration.getDelay(), configuration.getUnit());
    }

    public void vanish() {
        long delay = this.unit.convert(System.currentTimeMillis() - this.getLastAccessTime(), TimeUnit.MILLISECONDS);
        if (delay > this.inactivityDelay) {
            stop();
        }
    }

    public void stop() {
        this.trafficLimiter.remove(this);
        this.stopReplenishing();
        this.executor.shutdown();
    }
}
