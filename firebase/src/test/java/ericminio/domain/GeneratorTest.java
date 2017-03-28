package ericminio.domain;

import ericminio.firebase.Entry;
import ericminio.firebase.Firebase;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;

public class GeneratorTest {

    private Firebase database = new Firebase("https://sandbox-5f095.firebaseio.com/");

    @Ignore
    @Test
    public void go() throws Exception {
        RandomEvent randomEvent = new RandomEvent();
        randomEvent.setUsers(Arrays.asList("Tom67", "MeaThoN", "lyllikka", "dragon123"));
        randomEvent.setGames(Arrays.asList("Book of dead", "Thunderstruck II", "Well of wonder"));
        randomEvent.setEvents(Arrays.asList(new LoggedIn(), new LevelUp(), new GameWin()));

        while(true) {
            long timestamp = System.currentTimeMillis();
            String data = "{\"message\":\""+ randomEvent.please().description +"\",\"timestamp\":"+timestamp+"}";
            database.save(new Entry("news/" + timestamp, data));
            Thread.sleep(3000);
        }
    }
}
