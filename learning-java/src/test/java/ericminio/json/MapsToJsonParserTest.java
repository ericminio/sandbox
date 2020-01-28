package ericminio.json;

import org.junit.Test;

import java.util.*;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class MapsToJsonParserTest {

    @Test
    public void canHandleSimpleObject() {
        Map<String, Object> tree = new HashMap<>();
        tree.put("old", Boolean.TRUE);
        tree.put("obsolete", Boolean.FALSE);

        assertThat(MapsToJsonParser.stringify(tree), equalTo("{\"old\":true,\"obsolete\":false}"));
    }
    @Test
    public void canHandleNestedObject() {
        Map<String, Object> tree = new HashMap<>();
        Map<String, Object> attributes = new HashMap<>();
        tree.put("attributes", attributes);
        attributes.put("old", Boolean.TRUE);
        attributes.put("obsolete", Boolean.FALSE);

        assertThat(MapsToJsonParser.stringify(tree), equalTo("{\"attributes\":{\"old\":true,\"obsolete\":false}}"));
    }
    @Test
    public void canHandleNumbers() {
        Map<String, Object> tree = new LinkedHashMap<>();
        tree.put("first", Integer.valueOf(5));
        tree.put("second", Float.valueOf("5.5"));
        tree.put("third", Double.valueOf("12.34"));

        assertThat(MapsToJsonParser.stringify(tree), equalTo("{\"first\":5,\"second\":5.5,\"third\":12.34}"));
    }
    @Test
    public void canHandleCollections() {
        Map<String, Object> tree = new HashMap<>();
        List<Map> attributes = new ArrayList<>();
        tree.put("attributes", attributes);

        Map<String, Object> first = new HashMap<>();
        first.put("old", Boolean.TRUE);
        attributes.add(first);

        Map<String, Object> second = new HashMap<>();
        second.put("obsolete", Boolean.FALSE);
        attributes.add(second);

        assertThat(MapsToJsonParser.stringify(tree), equalTo("{\"attributes\":[{\"old\":true},{\"obsolete\":false}]}"));
    }
}
