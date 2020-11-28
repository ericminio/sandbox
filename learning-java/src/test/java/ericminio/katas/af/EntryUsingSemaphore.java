package ericminio.katas.af;

import java.util.concurrent.Semaphore;

public class EntryUsingSemaphore extends Semaphore implements Entry {

    public EntryUsingSemaphore(AfConfiguration configuration) {
        super(configuration.getPermits());
    }

    @Override
    public boolean isOpen() {
        return tryAcquire();
    }
}
