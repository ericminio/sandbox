package ericminio.demo.primefactors.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class TrafficLimiter implements Runnable {

    private TrafficLimiterConfiguration configuration;
    private ConcurrentHashMap<Object, TrafficEntry> traffic;
    private ScheduledExecutorService executor;

    @Autowired
    public TrafficLimiter(TrafficLimiterConfiguration configuration) {
        this.configuration = configuration;
        traffic = new ConcurrentHashMap<>();
        this.executor = Executors.newScheduledThreadPool(1);
        this.executor.scheduleWithFixedDelay(this, configuration.getDelay(), configuration.getDelay(), configuration.getUnit());
    }

    public boolean isOpen(Object key) {
        return getTrafficEntry(key).tryAccess().isOpen();
    }

    private TrafficEntry getTrafficEntry(Object key) {
        TrafficEntry trafficEntry = traffic.get(key);
        if (trafficEntry == null) {
            trafficEntry = new TrafficEntry(key, configuration);
            traffic.put(key, trafficEntry);
        }
        return trafficEntry;
    }

    public void remove(TrafficEntry trafficEntry) {
        traffic.remove(trafficEntry.getKey());
    }

    @Override
    public void run() {
        long now = System.currentTimeMillis();

        List<TrafficEntry> toBeRemoved = new ArrayList<>();
        traffic.values().forEach(trafficEntry -> {
            trafficEntry.replenish();
            long delay = configuration.getUnit().convert(now - trafficEntry.getLastAccessTime(), TimeUnit.MILLISECONDS);
            if (delay > configuration.getInactivityDelay()) {
                toBeRemoved.add(trafficEntry);
            }
        });
        toBeRemoved.forEach(trafficEntry -> remove(trafficEntry));
    }
}
