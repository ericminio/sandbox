package ericminio.concurrency;

import java.util.concurrent.Semaphore;

public class TrafficEntry extends Semaphore {

    private long lastAccessTime;
    private Object key;
    private int permits;

    public TrafficEntry(Object key, TrafficLimiterConfiguration configuration) {
        super(configuration.getPermits());
        this.key = key;
        this.permits = configuration.getPermits();
    }

    public TrafficState tryAccess() {
        this.lastAccessTime = System.currentTimeMillis();

        return new TrafficState(this.tryAcquire());
    }

    public void replenish() {
        this.drainPermits();
        this.release(this.permits);
    }

    public long getLastAccessTime() {
        return lastAccessTime;
    }

    public Object getKey() {
        return key;
    }
}
