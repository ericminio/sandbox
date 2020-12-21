package ericminio.testing;

public interface WaitStrategy {

    void waitForStarted(int port);
    void waitForStopped(int port);
}
