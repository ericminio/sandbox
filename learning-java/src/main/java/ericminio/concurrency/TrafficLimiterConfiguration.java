package ericminio.concurrency;

import java.util.concurrent.TimeUnit;

public class TrafficLimiterConfiguration {

    private int permits;
    private long delay;
    private long inactivityDelay;
    private TimeUnit unit;

    public int getPermits() {
        return permits;
    }

    public void setPermits(int permits) {
        this.permits = permits;
    }

    public long getDelay() {
        return delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    public long getInactivityDelay() {
        return inactivityDelay;
    }

    public void setInactivityDelay(long inactivityDelay) {
        this.inactivityDelay = inactivityDelay;
    }

    public TimeUnit getUnit() {
        return unit;
    }

    public void setUnit(TimeUnit unit) {
        this.unit = unit;
    }
}
