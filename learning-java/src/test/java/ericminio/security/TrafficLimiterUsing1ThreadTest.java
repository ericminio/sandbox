package ericminio.security;

public class TrafficLimiterUsing1ThreadTest extends TrafficLimiterTest {

    @Override
    protected TrafficLimiter getTrafficLimiter(TrafficLimiterConfiguration configuration) {
        return new TrafficLimiterUsing1Thread(configuration);
    }
}
