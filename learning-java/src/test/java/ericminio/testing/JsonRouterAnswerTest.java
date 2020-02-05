package ericminio.testing;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class JsonRouterAnswerTest {

    protected String extractFunctionName(String call) {
//        String token = "~call~";
//        int start = call.indexOf(token);
//        if (start == -1) { return null; }
//        String starting = call.substring(start + token.length());
//        String name = starting.substring(0, starting.indexOf("()"));
//        return name;
        Pattern pattern = Pattern.compile(".*~call~(.*)\\(\\).*");
        Matcher matcher = pattern.matcher(call);
        if (matcher.matches()) {
            String name = matcher.group(1);
            return name;
        }
        return null;
    }

    @Test
    public void canExtractFunctionName() {
        assertThat(extractFunctionName("~call~toto()"), equalTo("toto"));
    }

    @Test
    public void resistTrailingNoise() {
        assertThat(extractFunctionName("~call~toto()anything-after"), equalTo("toto"));
    }

    @Test
    public void resistLeadingNoise() {
        assertThat(extractFunctionName("anything-before~call~toto()anything-after"), equalTo("toto"));
    }

    @Test
    public void resistNoMatch() {
        assertThat(extractFunctionName("anything"), equalTo(null));
    }
}
