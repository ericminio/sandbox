package ericminio.testing;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import java.util.List;

public class ListMatcher extends BaseMatcher {

    private final List expected;
    private Object actual;

    public ListMatcher(List expected) {
        this.expected = expected;
    }

    public static ListMatcher equalList(List expected) {
        return new ListMatcher((expected));
    }

    @Override
    public boolean matches(Object actual) {
        this.actual = actual;

        List candidate = (List) actual;
        if (candidate.size() != expected.size()) { return false; }
        if (! candidate.containsAll(expected)) { return  false; }

        return true;
    }

    @Override
    public void describeTo(Description description) {
        description.appendValue(this.expected);
    }
}
