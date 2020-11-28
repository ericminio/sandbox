package ericminio.katas.mastermind.domain;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Feedback {

    public Integer blackCount;
    public Integer whiteCount;

    public Feedback(int blackCount, int whiteCount) {
        this.blackCount = blackCount;
        this.whiteCount = whiteCount;
    }

    public static Feedback from(String value) {
        Pattern compile = Pattern.compile("(\\d) black, (\\d) white");
        Matcher matcher = compile.matcher(value);
        matcher.find();
        return new Feedback(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)));
    }

    @Override
    public boolean equals(Object other) {
        if (! (other instanceof Feedback)) { return false; }

        Feedback candidate = (Feedback) other;
        return candidate.blackCount == this.blackCount && candidate.whiteCount == this.whiteCount;
    }
    @Override
    public String toString() {
        return "Feedback: " + blackCount + " black, " + whiteCount + " white";
    }
}
