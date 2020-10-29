package ericminio.concurrency;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TrafficLimiterUsing1Thread implements TrafficLimiter, Runnable {

    private TrafficLimiterConfiguration configuration;
    private ConcurrentHashMap<Object, StampedSemaphore> traffic;
    private ScheduledExecutorService executor;

    public TrafficLimiterUsing1Thread(TrafficLimiterConfiguration configuration) {
        this.configuration = configuration;
        traffic = new ConcurrentHashMap<>();
        this.executor = Executors.newScheduledThreadPool(1);
        this.executor.scheduleWithFixedDelay(this, configuration.getDelay(), configuration.getDelay(), configuration.getUnit());
    }

    @Override
    public void stop() {
        executor.shutdown();
    }

    @Override
    public int trafficSize() {
        return traffic.size();
    }

    @Override
    public boolean isLimitReachedFor(Object key) {
        return !vanishingEntryFor(key).isStillVisible();
    }

    private StampedSemaphore vanishingEntryFor(Object key) {
        StampedSemaphore stampedSemaphore = traffic.get(key);
        if (stampedSemaphore == null) {
            stampedSemaphore = new StampedSemaphore(key, configuration.getPermits());
            traffic.put(key, stampedSemaphore);
        }
        return stampedSemaphore;
    }

    @Override
    public void run() {
        traffic.values().forEach(stampedSemaphore -> stampedSemaphore.replenish() );

        long now = System.currentTimeMillis();
        List<StampedSemaphore> toBeRemoved = new ArrayList<>();
        traffic.values().forEach(stampedSemaphore -> {
            long delay = configuration.getUnit().convert(now - stampedSemaphore.getLastAccessTime(), TimeUnit.MILLISECONDS);
            if (delay > configuration.getInactivityDelay()) {
                toBeRemoved.add(stampedSemaphore);
            }
        });
        toBeRemoved.forEach(stampedSemaphore -> traffic.remove(stampedSemaphore.getKey()));
    }
}
