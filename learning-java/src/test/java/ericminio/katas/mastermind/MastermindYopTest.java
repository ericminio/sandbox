package ericminio.katas.mastermind;

import ericminio.katas.mastermind.domain.Combination;
import ericminio.katas.mastermind.domain.Feedback;
import ericminio.katas.mastermind.domain.Mastermind;
import ericminio.yop.Yop;
import ericminio.yop.YopTest;
import org.junit.Before;

import java.awt.*;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class MastermindYopTest extends YopTest {

    private Mastermind mastermind;
    private Combination hidden;
    private Combination guess;

    @Before
    public void sut() {
        mastermind = new Mastermind();
    }

    @Override
    protected String getYopFileName() {
        return "mastermind.yop";
    }


    @Yop(match = "the hidden colors are (.*)")
    public void setHiddenSet(String colors) {
        hidden = Combination.from(colors);

        assertThat(hidden.colors, equalTo(new Color[]{ Color.blue, Color.white, Color.red }));
    }
    @Yop(match = "Hidden: (.*)")
    public void setHiddenSetDifferently(String colors) {
        hidden = Combination.from(colors);

        assertThat(hidden.colors, equalTo(new Color[]{ Color.blue, Color.white, Color.red }));
    }

    @Yop(match = "the guess is (.*)")
    public void setGuess(String colors) {
        guess = Combination.from(colors);

        assertThat(guess.colors, equalTo(new Color[]{ Color.blue, Color.white, Color.red }));
    }

    @Yop(match = "the feedback is (.*)")
    public void expectFeedback(String result) {
        Feedback expected = Feedback.from(result);
        assertThat(new Feedback(3, 0), equalTo(expected));

        Feedback actual = mastermind.hides(hidden).evaluate(guess);
        assertThat(actual, equalTo(expected));
    }

    @Yop(match = "(.*) -> (.*)")
    public void setGuessAndExpectFeedback(String colors, String result) {
        Combination guess = Combination.from(colors);
        Feedback actual = mastermind.hides(hidden).evaluate(guess);

        assertThat(actual, equalTo(Feedback.from(result)));
    }
}
