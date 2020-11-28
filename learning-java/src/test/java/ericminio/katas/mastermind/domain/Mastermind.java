package ericminio.katas.mastermind.domain;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Mastermind {

    private Combination hidden;

    public Mastermind hides(Combination hidden) {
        this.hidden = hidden;
        return this;
    }

    public Feedback evaluate(Combination guess) {
        int blackCount = evaluateCorrectPositions(guess);

        ArrayList<Color> intersection = new ArrayList<>(Arrays.asList(hidden.colors));
        intersection.retainAll(new ArrayList<>(Arrays.asList(guess.colors)));
        int whiteCount = intersection.size() - blackCount;

        return new Feedback(blackCount, whiteCount);
    }

    private Integer evaluateCorrectPositions(Combination guess) {
        int black = 0;
        for (int i=0; i<guess.colors.length; i++) {
            if (hidden.colors[i] == guess.colors[i]) {
                black += 1;
            }
        }
        return black;
    }
}
