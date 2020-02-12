package ericminio.json;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static ericminio.json.JsonToMapsParser.parse;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class JsonToMapsParserTest {

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
    public void canParseTwoLevelsOfNestedObjects() {
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
    @Test
    public void canParseCollection() {
        String json = "{ \"attributes\": [ { \"key\": \"old\", \"value\": \"yes\" }, { \"key\": \"obsolete\", \"value\": \"no\" } ] }";
        Map<String, Object> tree = parse(json);

        assertThat(tree.size(), equalTo(1));
        assertThat(tree.get("attributes"), instanceOf(List.class));

        List<Map<String, Object>> values = (List<Map<String, Object>>) tree.get("attributes");
        assertThat(values.size(), equalTo(2));

        assertThat(values.get(0).get("key"), equalTo("old"));
        assertThat(values.get(0).get("value"), equalTo("yes"));

        assertThat(values.get(1).get("key"), equalTo("obsolete"));
        assertThat(values.get(1).get("value"), equalTo("no"));
    }
    @Test
    public void resistsMalformedJson() {
        String json = "{ looks like json: but no :(";
        try{
            parse(json);
            fail();
        }
        catch(RuntimeException e) {
            e.printStackTrace();
            assertThat(e.getMessage(), equalTo("Malformed json?"));
        }
    }
    @Test
    public void resistsIntegers() {
        String json = "{ \"answer\": 42 }";
        Map<String, Object> tree = parse(json);

        assertThat(tree.size(), equalTo(1));
        assertThat(tree.get("answer"), equalTo(42));
    }
    @Test
    public void preservesSpaceData() {
        String json = "{  \"obsolete\" :  \"of course not :)\"  }";
        Map<String, Object> tree = parse(json);

        assertThat(tree.size(), equalTo(1));
        assertThat(tree.get("obsolete"), equalTo("of course not :)"));
    }
    @Test
    public void resistsBigDecimals() {
        String json = "{ \"answer\": 4.2 }";
        Map<String, Object> tree = parse(json);

        assertThat(tree.size(), equalTo(1));
        assertThat(tree.get("answer"), equalTo(BigDecimal.valueOf(4.2)));
    }
    @Test
    public void resistsParenthesis() {
        String json = "{ \"answer\": \"(42)\" }";
        Map<String, Object> tree = parse(json);

        assertThat(tree.size(), equalTo(1));
        assertThat(tree.get("answer"), equalTo("(42)"));
    }
    @Test
    public void resistsEmptyCollection() {
        String json = "{ \"attributes\": [ ] }";
        Map<String, Object> tree = parse(json);

        assertThat(tree.size(), equalTo(1));
        assertThat(tree.get("attributes"), instanceOf(List.class));

        List<Map<String, Object>> values = (List<Map<String, Object>>) tree.get("attributes");
        assertThat(values.size(), equalTo(0));
    }
    @Test
    public void resistsNull() {
        String json = "{ \"attributes\": null }";
        Map<String, Object> tree = parse(json);

        assertThat(tree.size(), equalTo(1));
        assertThat(tree.get("attributes"), equalTo(null));
    }
}
