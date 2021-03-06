package ericminio.json;

import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class JsonToMapsParserTest {

    @Test
    public void canParseSingleAttribute() {
        String json = "{ \"alive\": true }";
        Map<String, Object> tree = (Map<String, Object>) new JsonToMapsParser().parse(json);

        assertThat(tree.size(), equalTo(1));
        assertThat(tree.get("alive"), equalTo(Boolean.TRUE));
    }
    @Test
    public void canParseSingleStringAttribute() {
        String json = "{ \"alive\": \"true\" }";
        Map<String, Object> tree = (Map<String, Object>) new JsonToMapsParser().parse(json);

        assertThat(tree.size(), equalTo(1));
        assertThat(tree.get("alive"), equalTo("true"));
    }
    @Test
    public void canParseTwoAttributes() {
        String json = "{ \"old\": true, \"obsolete\": false }";
        Map<String, Object> tree = (Map<String, Object>) new JsonToMapsParser().parse(json);

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
        Map<String, Object> tree = (Map<String, Object>) new JsonToMapsParser().parse(json);

        assertThat(tree.size(), equalTo(2));
        assertThat(tree.get("old"), equalTo(Boolean.TRUE));
        assertThat(tree.get("obsolete"), equalTo(Boolean.FALSE));
    }
    @Test
    public void canParseOneNestedObject() {
        String json = "{ \"attributes\": { \"old\": true, \"obsolete\": false } }";
        Map<String, Object> tree = (Map<String, Object>) new JsonToMapsParser().parse(json);

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
        Map<String, Object> tree = (Map<String, Object>) new JsonToMapsParser().parse(json);

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
        Map<String, Object> tree = (Map<String, Object>) new JsonToMapsParser().parse(json);

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
        Map<String, Object> tree = (Map<String, Object>) new JsonToMapsParser().parse(json);

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
        String json = "{ looks like json: \"but\" no :(";
        try{
            new JsonToMapsParser().parse(json);
            fail();
        }
        catch(RuntimeException e) {
            e.printStackTrace();
            assertThat(e.getMessage(), equalTo("Malformed json? Input was: { looks like json: \"but\" no :("));
        }
    }
    @Test
    public void resistsIntegers() {
        String json = "{ \"answer\": 42 }";
        Map<String, Object> tree = (Map<String, Object>) new JsonToMapsParser().parse(json);

        assertThat(tree.size(), equalTo(1));
        assertThat(tree.get("answer"), equalTo(42));
    }
    @Test
    public void preservesSpaceData() {
        String json = "{  \"obsolete\" :  \"of course not :)\"  }";
        Map<String, Object> tree = (Map<String, Object>) new JsonToMapsParser().parse(json);

        assertThat(tree.size(), equalTo(1));
        assertThat(tree.get("obsolete"), equalTo("of course not :)"));
    }
    @Test
    public void resistsDecimals() {
        String json = "{ \"answer\": 4.2 }";
        Map<String, Object> tree = (Map<String, Object>) new JsonToMapsParser().parse(json);

        assertThat(tree.size(), equalTo(1));
        assertThat(tree.get("answer"), equalTo(new Double(4.2)));
    }
    @Test
    public void resistsParenthesis() {
        String json = "{ \"answer\": \"(42)\" }";
        Map<String, Object> tree = (Map<String, Object>) new JsonToMapsParser().parse(json);

        assertThat(tree.size(), equalTo(1));
        assertThat(tree.get("answer"), equalTo("(42)"));
    }
    @Test
    public void resistsEmptyCollection() {
        String json = "{ \"attributes\": [ ] }";
        Map<String, Object> tree = (Map<String, Object>) new JsonToMapsParser().parse(json);

        assertThat(tree.size(), equalTo(1));
        assertThat(tree.get("attributes"), instanceOf(List.class));

        List<Map<String, Object>> values = (List<Map<String, Object>>) tree.get("attributes");
        assertThat(values.size(), equalTo(0));
    }
    @Test
    public void resistsNull() {
        String json = "{ \"attributes\": null }";
        Map<String, Object> tree = (Map<String, Object>) new JsonToMapsParser().parse(json);

        assertThat(tree.size(), equalTo(1));
        assertThat(tree.get("attributes"), equalTo(null));
    }
    @Test
    public void resistQueryString() {
       String json ="{\"url\":\"/any?field1=this&field2=that\"}";
        Map<String, Object> tree = (Map<String, Object>) new JsonToMapsParser().parse(json);

        assertThat(tree.size(), equalTo(1));
        assertThat(tree.get("url"), equalTo("/any?field1=this&field2=that"));
    }
    @Test
    public void preservesCommaInData() {
        String json = "{ \"attributes\": \"one, two\" }";
        Map<String, Object> tree = (Map<String, Object>) new JsonToMapsParser().parse(json);

        assertThat(tree.size(), equalTo(1));
        assertThat(tree.get("attributes"), equalTo("one, two"));
    }
    @Test
    public void preservesColonInData() {
        String json = "{ \"attributes\": \"one : two\" }";
        Map<String, Object> tree = (Map<String, Object>) new JsonToMapsParser().parse(json);

        assertThat(tree.size(), equalTo(1));
        assertThat(tree.get("attributes"), equalTo("one : two"));
    }
    @Test
    public void canParseString() {
        String json = "\"hello world\"";
        Object parsed = new JsonToMapsParser().parse(json);

        assertThat(parsed, instanceOf(String.class));
        assertThat((String) parsed, equalTo("hello world"));
    }
    @Test
    public void canParseInteger() {
        String json = "15";
        Object parsed = new JsonToMapsParser().parse(json);

        assertThat(parsed, instanceOf(Integer.class));
        assertThat((Integer) parsed, equalTo(Integer.valueOf("15")));
    }
    @Test
    public void canParseBoolean() {
        String json = "true";
        Object parsed = new JsonToMapsParser().parse(json);

        assertThat(parsed, instanceOf(Boolean.class));
        assertThat((Boolean) parsed, equalTo(Boolean.TRUE));
    }
    @Test
    public void canParseCollectionOfStrings() {
        String json = "{ \"codes\": [ \"one\", \"two\" ] }";
        Object parsed = new JsonToMapsParser().parse(json);

        assertThat(parsed, instanceOf(Map.class));
        Map<String, Object> tree = (Map<String, Object>) parsed;
        assertThat(tree.size(), equalTo(1));

        assertThat(tree.get("codes"), instanceOf(List.class));
        List<String> values = (List<String>) tree.get("codes");

        assertThat(values.size(), equalTo(2));
        assertThat(values.get(0), equalTo("one"));
        assertThat(values.get(1), equalTo("two"));
    }
}
