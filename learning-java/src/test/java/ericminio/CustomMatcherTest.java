package ericminio;

import org.junit.Test;

import java.util.List;

import static ericminio.ListMatcher.equalList;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertThat;

public class CustomMatcherTest {

    @Test
    public void worksWithBasicAssertions() {
        List actual = asList("hello", "world");

        assertThat(actual, equalList(asList("hello", "world")));
    }
}
