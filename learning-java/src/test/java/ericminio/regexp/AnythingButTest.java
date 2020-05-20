package ericminio.regexp;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class AnythingButTest {

    @Test
    public void canBeAchievedWithCircumflex() {
        Pattern pattern = Pattern.compile("^[^a]");

        Matcher matcher = pattern.matcher("abc");
        assertThat(matcher.find(), equalTo(false));

        matcher = pattern.matcher("bc");
        assertThat(matcher.find(), equalTo(true));
    }
    @Test
    public void worksWithSeveralExclusions() {
        Pattern pattern = Pattern.compile("[^a^b]");

        Matcher matcher = pattern.matcher("ab");
        assertThat(matcher.find(), equalTo(false));

        matcher = pattern.matcher("cd");
        assertThat(matcher.find(), equalTo(true));
    }

    private String opening(String tag) {
        return "<[^/]*:?" + tag + "[^<]*>";
    }

    private String closing(String tag) {
        return "</[^>]*:?" + tag + ">";
    }

    @Test
    public void usefulToSelectOpeningTagWithOrWithoutNamespace() {
        String tag = "hello";
        Pattern pattern = Pattern.compile(opening(tag));

        Matcher matcher = pattern.matcher("<ns:hello from=\"me\">world<any>inside</any></ns:hello>");
        assertThat(matcher.find(), equalTo(true));
        assertThat(matcher.start(), equalTo(0));
        assertThat(matcher.end(), equalTo("<ns:hello from=\"me\">".length()));
        assertThat(matcher.find(), equalTo(false));

        matcher = pattern.matcher("<hello from=\"me\">world<any>inside</any></hello>");
        assertThat(matcher.find(), equalTo(true));
        assertThat(matcher.start(), equalTo(0));
        assertThat(matcher.end(), equalTo("<hello from=\"me\">".length()));
        assertThat(matcher.find(), equalTo(false));
    }

    @Test
    public void usefulToSelectClosingTagWithOrWithoutNamespace() {
        String tag = "hello";
        Pattern pattern = Pattern.compile(closing(tag));

        Matcher matcher = pattern.matcher("<ns:hello from=\"me\">world<any>inside</any></ns:hello>anything after");
        assertThat(matcher.find(), equalTo(true));
        assertThat(matcher.start(), equalTo("<ns:hello from=\"me\">world<any>inside</any>".length()));
        assertThat(matcher.end(), equalTo("<ns:hello from=\"me\">world<any>inside</any></ns:hello>".length()));
        assertThat(matcher.find(), equalTo(false));
    }
}
