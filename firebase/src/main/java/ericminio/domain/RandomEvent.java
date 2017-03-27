package ericminio.domain;

import java.util.List;

public class RandomEvent {

    private List<String> users;
    private List<String> games;
    private List<Event> events;

    public void setUsers(List<String> users) {
        this.users = users;
    }

    public void setGames(List<String> games) {
        this.games = games;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public EventData please() {
        String user = this.users.get((int) (Math.random() * this.users.size()));
        String game = this.games.get((int) (Math.random() * this.games.size()));
        Event event = this.events.get((int) (Math.random() * this.events.size()));

        return event.data(user, game);
    }
}
