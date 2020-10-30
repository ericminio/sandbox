package ericminio.concurrency;

import java.util.concurrent.Semaphore;

public class TrafficEntry extends Semaphore {

    private long lastAccessTime;
    private Object key;
    private int permits;

    public TrafficEntry(Object key, int permits) {
        super(permits);
        this.key = key;
        this.permits = permits;
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
