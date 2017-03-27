package ericminio.firebase;

import ericminio.data.GameRepository;
import ericminio.data.UserRepository;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class NewsGeneratorTest {

    private Firebase database;
    private String url = "https://sandbox-5f095.firebaseio.com/";

    @Before
    public void givenOurDatabase() throws Exception {
        database = new Firebase(url);
        database.delete("games");
        database.save(new Entry("games/" + System.currentTimeMillis(), "{\"name\":\"Monopoly\"}"));
        database.save(new Entry("games/" + System.currentTimeMillis(), "{\"name\":\"Chess\"}"));
        database.save(new Entry("games/" + System.currentTimeMillis(), "{\"name\":\"Tarot\"}"));
        database.save(new Entry("games/" + System.currentTimeMillis(), "{\"name\":\"Black Jack\"}"));
        database.delete("users");
        database.save(new Entry("users/" + System.currentTimeMillis(), "{\"name\":\"Alice\"}"));
        database.save(new Entry("users/" + System.currentTimeMillis(), "{\"name\":\"Eric\"}"));
        database.save(new Entry("users/" + System.currentTimeMillis(), "{\"name\":\"Galak\"}"));
    }

    @Test
    public void someGamesExists() throws Exception {
        GameRepository storage = new FirebaseRepository(database);
        List<String> names = storage.allGames();

        assertThat(names, equalTo(Arrays.asList("Monopoly", "Chess", "Tarot", "Black Jack")));
    }

    @Test
    public void someUsersExists() throws Exception {
        UserRepository storage = new FirebaseRepository(database);
        List<String> names = storage.allUsers();

        assertThat(names, equalTo(Arrays.asList("Alice", "Eric", "Galak")));
    }
}
