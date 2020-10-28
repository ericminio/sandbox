package ericminio.demo.primefactors.security;

import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class SemaphoreWithTimedReplenishment extends Semaphore {

    public SemaphoreWithTimedReplenishment(int permits, int delay, TimeUnit unit) {
        super(permits);
        Executors.newScheduledThreadPool(1).scheduleWithFixedDelay(
                new Replenisher(this, permits), delay, delay, unit);
    }

    class Replenisher implements Runnable {

        private final Semaphore semaphore;
        private final int permits;

        public Replenisher(Semaphore semaphore, int permits) {
            this.semaphore = semaphore;
            this.permits = permits;
        }

        @Override
        public void run() {
            semaphore.drainPermits();
            semaphore.release(permits);
        }
    }
}
