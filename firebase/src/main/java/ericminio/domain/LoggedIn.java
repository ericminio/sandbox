package ericminio.domain;

public class LoggedIn implements Event {

    @Override
    public EventData data(String user, String game) {
        return new EventData(user, game) {{
            this.description = this.user + " logged in. Best of luck today, my good friend.";
        }};
    }
}
