package ericminio.concurrency;

public class TrafficState {

    private boolean isOpen;

    public TrafficState(boolean isOpen) {
        this.isOpen = isOpen;
    }

    public boolean isOpen() {
        return isOpen;
    }
}