package ericminio.security;

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
        traffic.values().forEach(trafficEntryVanishing -> trafficEntryVanishing.stop());
    }

    @Override
    public int trafficSize() {
        return traffic.size();
    }

    @Override
    public boolean isOpen(Object key) {
        return getTrafficEntry(key).tryAccess().isOpen();
    }

    private TrafficEntryVanishing getTrafficEntry(Object key) {
        TrafficEntryVanishing trafficEntryVanishing = traffic.get(key);
        if (trafficEntryVanishing == null) {
            trafficEntryVanishing = new TrafficEntryVanishing(key, configuration, this);
            traffic.put(key, trafficEntryVanishing);
        }
        return trafficEntryVanishing;
    }

    @Override
    public void remove(TrafficEntry trafficEntry) {
        this.traffic.remove(trafficEntry.getKey());
    }
}
