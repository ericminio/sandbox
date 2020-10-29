package ericminio.concurrency;

import java.util.concurrent.*;

public class TrafficLimiterUsing2nThreads implements TrafficLimiter {

    private TrafficLimiterConfiguration configuration;
    private ConcurrentHashMap<Object, TrafficVanishingEntry> traffic;

    public TrafficLimiterUsing2nThreads(TrafficLimiterConfiguration configuration) {
        this.configuration = configuration;
        traffic = new ConcurrentHashMap<>();
    }

    @Override
    public void stop() {
        traffic.values().forEach(cleaner -> cleaner.clean());
    }

    @Override
    public int trafficSize() {
        return traffic.size();
    }

    @Override
    public boolean isLimitReachedFor(Object key) {
        return !vanishingEntryFor(key).isStillVisible();
    }

    private TrafficVanishingEntry vanishingEntryFor(Object key) {
        TrafficVanishingEntry trafficVanishingEntry = traffic.get(key);
        if (trafficVanishingEntry == null) {
            trafficVanishingEntry = new TrafficVanishingEntry(key, this);
            traffic.put(key, trafficVanishingEntry);
        }
        return trafficVanishingEntry;
    }

    public void remove(TrafficVanishingEntry vanished) {
        this.traffic.remove(vanished.getKey());
    }

    public TrafficLimiterConfiguration getConfiguration() {
        return configuration;
    }
}
