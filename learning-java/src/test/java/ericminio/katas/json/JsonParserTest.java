package ericminio.katas.json;

import ericminio.json.JsonToMapsParser;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class JsonParserTest {

    @Test
    public void canParseSingleBooleanAttribute() {
        String json = "{ \"alive\": true }";
        Map<String, Object> tree = new JsonToMapsParser().parse(json);

        assertThat(tree.size(), equalTo(1));
        assertThat(tree.get("alive"), equalTo(Boolean.TRUE));
    }
    @Test
    public void canParseSingleStringAttribute() {
        String json = "{ \"alive\": \"yes\" }";
        Map<String, Object> tree = new JsonToMapsParser().parse(json);

        assertThat(tree.size(), equalTo(1));
        assertThat(tree.get("alive"), equalTo("yes"));
    }
    @Test
    public void preservesSpaceInData() {
        String json = "{ \"alive\": \"yes Sir\" }";
        Map<String, Object> tree = new JsonToMapsParser().parse(json);

        assertThat(tree.size(), equalTo(1));
        assertThat(tree.get("alive"), equalTo("yes Sir"));
    }
    @Test
    public void preservesSpecialCharactersInData() {
        String json = "{ \"alive\": \"Indeed, because: I'm still young you! [{}}]\\\", trap: \\\"'$%\" }";
        Map<String, Object> tree = new JsonToMapsParser().parse(json);

        assertThat(tree.size(), equalTo(1));
        assertThat(tree.get("alive"), equalTo("Indeed, because: I'm still young you! [{}}]\", trap: \"'$%"));
    }
    @Test
    public void canParseTwoAttributes() {
        String json = "{ \"alive\": true, \"status\": \"operational\" }";
        Map<String, Object> tree = new JsonToMapsParser().parse(json);

        assertThat(tree.size(), equalTo(2));
        assertThat(tree.get("alive"), equalTo(Boolean.TRUE));
        assertThat(tree.get("status"), equalTo("operational"));
    }
    @Test
    public void canParseNestedObject() {
        String json = "{ \"report\": { \"old\": true, \"obsolete\": false }, \"status\": \"operational\" }";
        Map<String, Object> tree = new JsonToMapsParser().parse(json);

        assertThat(tree.size(), equalTo(2));
        assertThat(tree.get("status"), equalTo("operational"));
        Map<String, Object> report = (Map<String, Object>) tree.get("report");
        assertThat(report.size(), equalTo(2));
        assertThat(report.get("old"), equalTo(Boolean.TRUE));
        assertThat(report.get("obsolete"), equalTo(Boolean.FALSE));
    }
    @Test
    public void resistsSpecialCharactersInNestedObjectData() {
        String json = "{ \"report\": { \"alive\": \"Indeed, because: I'm still young you! [{}}]\\\", trap: \\\"'$%\" } }";
        Map<String, Object> tree = new JsonToMapsParser().parse(json);

        assertThat(tree.size(), equalTo(1));
        Map<String, Object> report = (Map<String, Object>) tree.get("report");
        assertThat(report.get("alive"), equalTo("Indeed, because: I'm still young you! [{}}]\", trap: \"'$%"));
    }
    @Test
    public void canParseSecondLevelNestedObject() {
        String json = "{ \"report\": { \"status\": { \"old\": true, \"obsolete\": false } } }";
        Map<String, Object> tree = new JsonToMapsParser().parse(json);

        assertThat(tree.size(), equalTo(1));
        Map<String, Object> report = (Map<String, Object>) tree.get("report");
        assertThat(report.size(), equalTo(1));
        Map<String, Object> status = (Map<String, Object>) report.get("status");
        assertThat(status.get("old"), equalTo(Boolean.TRUE));
        assertThat(status.get("obsolete"), equalTo(Boolean.FALSE));
    }
    @Test
    public void canParseCollection() {
        String json = "{ \"people\": [{\"name\":\"you\"}, {\"name\":\"me\"}] }";
        Map<String, Object> tree = new JsonToMapsParser().parse(json);

        assertThat(tree.size(), equalTo(1));
        List<Object> people = (List<Object>) tree.get("people");
        assertThat(people.size(), equalTo(2));
        Map<Object, String> first = (Map<Object, String>) people.get(0);
        assertThat(first.get("name"), equalTo("you"));
        Map<Object, String> second = (Map<Object, String>) people.get(1);
        assertThat(second.get("name"), equalTo("me"));
    }
    @Test
    public void resistsEmptyCollection() {
        String json = "{ \"people\": [] }";
        Map<String, Object> tree = new JsonToMapsParser().parse(json);

        assertThat(tree.size(), equalTo(1));
        List<Object> people = (List<Object>) tree.get("people");
        assertThat(people.size(), equalTo(0));
    }
    @Test
    public void resistsEmptyObject() {
        String json = "{}";
        Map<String, Object> tree = new JsonToMapsParser().parse(json);

        assertThat(tree.size(), equalTo(0));
    }
    @Test
    public void canParseNumbers() {
        String json = "{ \"one\": 42, \"two\": 15.09 }";
        Map<String, Object> tree = new JsonToMapsParser().parse(json);

        assertThat(tree.size(), equalTo(2));
        assertThat(tree.get("one"), equalTo(new Integer(42)));
        assertThat(tree.get("two"), equalTo(new Double(15.09)));
    }
    @Test
    public void resistsIndentation() {
        String json = "{\n\t\"old\": true,\n\t\"obsolete\": false\n}";
        Map<String, Object> tree = new JsonToMapsParser().parse(json);

        assertThat(tree.size(), equalTo(2));
        assertThat(tree.get("old"), equalTo(Boolean.TRUE));
        assertThat(tree.get("obsolete"), equalTo(Boolean.FALSE));
    }
    @Test
    public void resistsExtraSpaces() {
        String json = "{  \"obsolete\"   :  \"no\"  }";
        Map<String, Object> tree = new JsonToMapsParser().parse(json);

        assertThat(tree.size(), equalTo(1));
        assertThat(tree.get("obsolete"), equalTo("no"));
    }
    @Test
    public void resistsNull() {
        String json = "{ \"attributes\": null }";
        Map<String, Object> tree = new JsonToMapsParser().parse(json);

        assertThat(tree.size(), equalTo(1));
        assertThat(tree.get("attributes"), equalTo(null));
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
            assertThat(e.getMessage(), equalTo("Malformed json? Input was: " + json));
        }
    }
}

