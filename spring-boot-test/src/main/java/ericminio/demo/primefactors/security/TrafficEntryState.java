package ericminio.demo.primefactors.security;

public class TrafficEntryState {

    private boolean isOpen;

    public TrafficEntryState(boolean isOpen) {
        this.isOpen = isOpen;
    }

    public boolean isOpen() {
        return isOpen;
    }
}
