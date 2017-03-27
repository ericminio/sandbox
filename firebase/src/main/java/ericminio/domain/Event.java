package ericminio.domain;

public interface Event {
    EventData data(String user, String game);
}
