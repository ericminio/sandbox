package ericminio.demo.primefactors.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class TrafficLimiterConfiguration {

    @Value("${trafficLimiter.permits}")
    private int permits;

    @Value("${trafficLimiter.delay}")
    private long delay;

    @Value("${trafficLimiter.inactivityDelay}")
    private long inactivityDelay;

    @Value("${trafficLimiter.unit}")
    private String unitName;

    public long getDelay() {
        return delay;
    }

    public int getPermits() {
        return permits;
    }

    public long getInactivityDelay() {
        return inactivityDelay;
    }

    public TimeUnit getUnit() {
        return TimeUnit.valueOf(unitName);
    }
}
