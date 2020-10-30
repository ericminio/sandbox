package ericminio.concurrency;

import java.util.concurrent.*;

public class TrafficEntryReplenishing extends TrafficEntry {

    private final ScheduledExecutorService scheduledExecutorService;

    public TrafficEntryReplenishing(Object key, TrafficLimiterConfiguration configuration) {
        super(key, configuration);
        this.scheduledExecutorService = Executors.newScheduledThreadPool(1);
        this.scheduledExecutorService.scheduleWithFixedDelay(() -> replenish(), configuration.getDelay(), configuration.getDelay(), configuration.getUnit());
    }

    public void stopReplenishing() {
        this.scheduledExecutorService.shutdown();
    }
}
