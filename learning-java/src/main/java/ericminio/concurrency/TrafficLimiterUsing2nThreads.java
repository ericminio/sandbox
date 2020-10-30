package ericminio.concurrency;

import java.util.concurrent.*;

public class TrafficLimiterUsing2nThreads implements TrafficLimiter {

    private TrafficLimiterConfiguration configuration;
    private ConcurrentHashMap<Object, TrafficEntryVanishing> traffic;

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
    public boolean isOpen(Object key) {
        return !getTrafficEntry(key).isStillVisible();
    }

    private TrafficEntryVanishing getTrafficEntry(Object key) {
        TrafficEntryVanishing trafficEntryVanishing = traffic.get(key);
        if (trafficEntryVanishing == null) {
            trafficEntryVanishing = new TrafficEntryVanishing(key, this);
            traffic.put(key, trafficEntryVanishing);
        }
        return trafficEntryVanishing;
    }

    public void remove(TrafficEntryVanishing vanished) {
        this.traffic.remove(vanished.getKey());
    }

    public TrafficLimiterConfiguration getConfiguration() {
        return configuration;
    }
}
