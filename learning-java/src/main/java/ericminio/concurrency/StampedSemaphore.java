package ericminio.concurrency;

import java.util.concurrent.Semaphore;

public class StampedSemaphore extends Semaphore {

    private long lastAccessTime;
    private Object key;
    private int permits;

    public StampedSemaphore(Object key, int permits) {
        super(permits);
        this.key = key;
        this.permits = permits;
    }

    public boolean isStillVisible() {
        this.lastAccessTime = System.currentTimeMillis();
        return this.tryAcquire();
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
