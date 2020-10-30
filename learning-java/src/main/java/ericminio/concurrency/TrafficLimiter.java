package ericminio.concurrency;

public interface TrafficLimiter {

    boolean isOpen(Object key);

    int trafficSize();

    void stop();

    void remove(TrafficEntry trafficEntry);
}
