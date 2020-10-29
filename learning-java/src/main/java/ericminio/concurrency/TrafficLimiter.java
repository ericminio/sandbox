package ericminio.concurrency;

public interface TrafficLimiter {

    boolean isLimitReachedFor(Object key);

    int trafficSize();

    void stop();
}
