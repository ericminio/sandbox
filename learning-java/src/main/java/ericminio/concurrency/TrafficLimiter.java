package ericminio.concurrency;

import java.util.concurrent.*;

public class TrafficLimiter {

    private TrafficLimiterConfiguration configuration;
    private ConcurrentHashMap<Object, TrafficVanishingEntry> traffic;

    public TrafficLimiter() {
        traffic = new ConcurrentHashMap<>();
    }

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

    public void setConfiguration(TrafficLimiterConfiguration configuration) {
        this.configuration = configuration;
    }

    public TrafficLimiterConfiguration getConfiguration() {
        return configuration;
    }

    public ConcurrentHashMap<Object, TrafficVanishingEntry> getTraffic() {
        return traffic;
    }

    public int trafficSize() {
        return traffic.size();
    }
}
