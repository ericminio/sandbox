package ericminio.domain;

public class LevelUp implements Event {

    @Override
    public EventData data(String user, String game) {
        return new EventData(user, game) {{
            this.description = this.user + " leveled up in " + this.game + ". Well done!";
        }};
    }
}
