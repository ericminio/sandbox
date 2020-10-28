package ericminio.demo.primefactors.security;

import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

@Service
public class RateLimiter {

    ConcurrentHashMap<Integer, SemaphoreWithTimedReplenishment> traffic;

    public RateLimiter() {
        traffic = new ConcurrentHashMap<>();
    }

    public boolean limitIsReachedFor(Integer number) {
        Semaphore semaphore = get(number);
        return !semaphore.tryAcquire();
    }

    private Semaphore get(Integer number) {
        SemaphoreWithTimedReplenishment semaphore = traffic.get(number);
        if (semaphore == null) {
            semaphore = new SemaphoreWithTimedReplenishment(1, 2, TimeUnit.SECONDS);
            traffic.put(number, semaphore);
        }
        return semaphore;
    }
}
