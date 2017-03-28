package ericminio.domain;

import ericminio.firebase.Entry;
import ericminio.firebase.Firebase;
import ericminio.firebase.FirebaseRepository;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;

public class GeneratorTest {

    private Firebase database = new Firebase("https://sandbox-5f095.firebaseio.com/");
    RandomEvent randomEvent;

    @Before
    public void getData() throws Exception {
        FirebaseRepository storage = new FirebaseRepository(database);
        randomEvent = new RandomEvent();
        randomEvent.setUsers(storage.allUsers());
        randomEvent.setGames(storage.allGames());
        randomEvent.setEvents(Arrays.asList(new LoggedIn(), new LevelUp(), new GameWin()));
    }

    @Ignore
    @Test
    public void go() throws Exception {

        while(true) {
            long timestamp = System.currentTimeMillis();
            String data = "{\"message\":\""+ randomEvent.please().description +"\",\"timestamp\":"+timestamp+"}";
            database.save(new Entry("news/" + timestamp, data));
            Thread.sleep(5000);
        }
    }
}
