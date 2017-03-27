package ericminio.domain;

import ericminio.domain.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

public class RandomEventTest {

    RandomEvent randomEvent;

    @Before
    public void initReferenceData() {
        randomEvent = new RandomEvent();
        randomEvent.setUsers(Arrays.asList("you"));
        randomEvent.setGames(Arrays.asList("tictactoe"));
        randomEvent.setEvents(Arrays.asList(new LevelUp()));
    }

    @Test
    public void levelUp() {
        randomEvent.setEvents(Arrays.asList(new LevelUp()));
        EventData data = randomEvent.please();

        assertThat(data.description, containsString("you leveled up in tictactoe"));
    }

    @Test
    public void loggedInt() {
        randomEvent.setEvents(Arrays.asList(new LoggedIn()));
        EventData data = randomEvent.please();

        assertThat(data.description, containsString("you logged in. Best of luck today, my good friend."));
    }

    @Test
    public void gameWin() {
        randomEvent.setEvents(Arrays.asList(new GameWin()));
        EventData data = randomEvent.please();

        assertThat(data.description, containsString("you just won $"));
    }
}
