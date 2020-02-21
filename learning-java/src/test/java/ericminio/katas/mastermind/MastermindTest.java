package ericminio.katas.mastermind;

import org.junit.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class MastermindTest {

    @Test
    public void canDetectOneCorrectPositionsCount() { ;
        Feedback feedback = new CodeMaker()
                .hides(Color.BLUE, Color.RED)
                .evaluate(Color.BLUE, Color.GREEN);

        assertThat(feedback.blackCount, equalTo(1));
        assertThat(feedback.whiteCount, equalTo(0));
    }
    @Test
    public void canDetectOneIncorrectPositionsCount() { ;
        Feedback feedback = new CodeMaker()
                .hides(Color.BLUE, Color.RED)
                .evaluate(Color.RED, Color.GREEN);

        assertThat(feedback.blackCount, equalTo(0));
        assertThat(feedback.whiteCount, equalTo(1));
    }
    @Test
    public void handlesDuplicatesAsExpected() { ;
        Feedback feedback = new CodeMaker()
                .hides(Color.BLUE, Color.GREEN, Color.BLUE, Color.YELLOW)
                .evaluate(Color.BLUE, Color.BLUE, Color.YELLOW, Color.BLUE);

        assertThat(feedback.blackCount, equalTo(1));
        assertThat(feedback.whiteCount, equalTo(2));
    }

    @Test
    public void confirmThatWeCanDetectSeveralCorrectPositionsCount() { ;
        Feedback feedback = new CodeMaker()
                .hides(Color.BLUE, Color.RED)
                .evaluate(Color.BLUE, Color.RED);

        assertThat(feedback.blackCount, equalTo(2));
        assertThat(feedback.whiteCount, equalTo(0));
    }
    @Test
    public void confirmThatWeCanDetectSeveralIncorrectPositionsCount() { ;
        Feedback feedback = new CodeMaker()
                .hides(Color.BLUE, Color.RED)
                .evaluate(Color.RED, Color.BLUE);

        assertThat(feedback.blackCount, equalTo(0));
        assertThat(feedback.whiteCount, equalTo(2));
    }
    @Test
    public void confirmThatWeCanDigestMixedFeedback() { ;
        Feedback feedback = new CodeMaker()
                .hides(Color.BLUE, Color.RED, Color.GREEN, Color.YELLOW)
                .evaluate(Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW);

        assertThat(feedback.blackCount, equalTo(2));
        assertThat(feedback.whiteCount, equalTo(2));
    }

    class CodeMaker {

        private Color[] code;

        public CodeMaker hides(Color... pegs) {
            this.code = pegs;
            return this;
        }

        public Feedback evaluate(Color... guesses) {
            Feedback feedback = new Feedback();
            feedback.blackCount = evaluateCorrectPositions(guesses);

            ArrayList<Color> intersection = new ArrayList<>(Arrays.asList(code));
            intersection.retainAll(new ArrayList<>(Arrays.asList(guesses)));
            feedback.whiteCount = intersection.size() - feedback.blackCount;

            return feedback;
        }

        private Integer evaluateCorrectPositions(Color[] guesses) {
            int black = 0;
            for (int i=0; i<guesses.length; i++) {
                Color guess = guesses[i];
                if (code[i] == guess) {
                    black += 1;
                }
            }
            return black;
        }

    }

    class Feedback {

        public Integer blackCount = 0;
        public Integer whiteCount = 0;
    }
}
