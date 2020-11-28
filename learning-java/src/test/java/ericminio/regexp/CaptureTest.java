package ericminio.regexp;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class CaptureTest {

    @Test
    public void simpleMatch() {
        Pattern pattern = Pattern.compile("hello");
        Matcher matcher = pattern.matcher("hello world");

        assertThat(matcher.find(), equalTo(true));
        assertThat(matcher.group(0), equalTo("hello"));
    }

    @Test
    public void capturingOneWord() {
        Pattern pattern = Pattern.compile("hello (.*)");
        Matcher matcher = pattern.matcher("hello world");

        assertThat(matcher.find(), equalTo(true));
        assertThat(matcher.group(0), equalTo("hello world"));
        assertThat(matcher.group(1), equalTo("world"));
    }

    @Test
    public void capturingSeveralWords() {
        Pattern pattern = Pattern.compile("hello (.*)");
        Matcher matcher = pattern.matcher("hello my friend");

        assertThat(matcher.find(), equalTo(true));
        assertThat(matcher.group(0), equalTo("hello my friend"));
        assertThat(matcher.group(1), equalTo("my friend"));
    }

    @Test
    public void capturingListOfSeveralWords() {
        Pattern pattern = Pattern.compile("hello (.*)");
        Matcher matcher = pattern.matcher("hello Bob, Jack, and Oliver");

        assertThat(matcher.find(), equalTo(true));
        assertThat(matcher.group(0), equalTo("hello Bob, Jack, and Oliver"));
        assertThat(matcher.group(1), equalTo("Bob, Jack, and Oliver"));
    }

    @Test
    public void capturingListOfSeveralWordsFromPartialMatch() {
        Pattern pattern = Pattern.compile("hello (.*)");
        Matcher matcher = pattern.matcher("a big hello Bob, Jack, and Oliver");

        assertThat(matcher.find(), equalTo(true));
        assertThat(matcher.group(0), equalTo("hello Bob, Jack, and Oliver"));
        assertThat(matcher.group(1), equalTo("Bob, Jack, and Oliver"));
    }

    @Test
    public void exploring() {
        Pattern pattern = Pattern.compile("the feedback is (.*)");
        Matcher matcher = pattern.matcher("    Then the feedback is 3 black, 0 white");

        assertThat(matcher.find(), equalTo(true));
        assertThat(matcher.group(0), equalTo("the feedback is 3 black, 0 white"));
        assertThat(matcher.group(1), equalTo("3 black, 0 white"));
    }
}
