package ericminio.domain;

public class GameWin implements Event {

    @Override
    public EventData data(String user, String game) {
        int price = (int) (5 + Math.random() * 20);
        return new EventData(user, game) {{
            this.description = this.user + " just won $" + price + " in " + this.game + ", wonderful!";
        }};
    }
}
