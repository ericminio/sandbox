package ericminio.json;

import org.junit.Test;

import java.util.Map;

import static ericminio.json.JsonToMapsParser.parse;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

public class JsonToMapParserTest {

    @Test
    public void canParseSingleAttribute() {
        String json = "{ \"alive\": true }";
        Map<String, Object> tree = parse(json);

        assertThat(tree.size(), equalTo(1));
        assertThat(tree.get("alive"), equalTo(Boolean.TRUE));
    }
    @Test
    public void canParseTwoAttributes() {
        String json = "{ \"old\": true, \"obsolete\": false }";
        Map<String, Object> tree = parse(json);

        assertThat(tree.size(), equalTo(2));
        assertThat(tree.get("old"), equalTo(Boolean.TRUE));
        assertThat(tree.get("obsolete"), equalTo(Boolean.FALSE));
    }
    @Test
    public void resistsIndentation() {
        String json = "{\n" +
                "\t\"old\": true,\n " +
                "\t\"obsolete\": false\n" +
                "}";
        Map<String, Object> tree = parse(json);

        assertThat(tree.size(), equalTo(2));
        assertThat(tree.get("old"), equalTo(Boolean.TRUE));
        assertThat(tree.get("obsolete"), equalTo(Boolean.FALSE));
    }
    @Test
    public void canParseOneNestedObject() {
        String json = "{ \"attributes\": { \"old\": true, \"obsolete\": false } }";
        Map<String, Object> tree = parse(json);

        assertThat(tree.size(), equalTo(1));
        assertThat(tree.get("attributes"), instanceOf(Map.class));

        Map<String, Object> attributes = (Map<String, Object>) tree.get("attributes");
        assertThat(attributes.size(), equalTo(2));
        assertThat(attributes.get("old"), equalTo(Boolean.TRUE));
        assertThat(attributes.get("obsolete"), equalTo(Boolean.FALSE));
    }
    @Test
    public void canParseTwoNestedObjects() {
        String json = "{ \"old\": { \"value\": true }, \"obsolete\": { \"value\": false } }";
        Map<String, Object> tree = parse(json);

        assertThat(tree.size(), equalTo(2));
        assertThat(tree.get("old"), instanceOf(Map.class));
        assertThat(tree.get("obsolete"), instanceOf(Map.class));

        Map<String, Object> old = (Map<String, Object>) tree.get("old");
        assertThat(old.size(), equalTo(1));
        assertThat(old.get("value"), equalTo(Boolean.TRUE));

        Map<String, Object> obsolete = (Map<String, Object>) tree.get("obsolete");
        assertThat(obsolete.size(), equalTo(1));
        assertThat(obsolete.get("value"), equalTo(Boolean.FALSE));
    }
    @Test
    public void canParseTwoLevelsNestedObject() {
        String json = "{ \"attributes\": { \"old\": true, \"but\": { \"obsolete\": false } } }";
        Map<String, Object> tree = parse(json);

        assertThat(tree.size(), equalTo(1));
        assertThat(tree.get("attributes"), instanceOf(Map.class));

        Map<String, Object> attributes = (Map<String, Object>) tree.get("attributes");
        assertThat(attributes.size(), equalTo(2));
        assertThat(attributes.get("old"), equalTo(Boolean.TRUE));
        assertThat(attributes.get("but"), instanceOf(Map.class));

        Map<String, Object> but = (Map<String, Object>) attributes.get("but");
        assertThat(but.size(), equalTo(1));
        assertThat(but.get("obsolete"), equalTo(Boolean.FALSE));
    }
}
