package ericminio.katas.json;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class JsonParserTest {

    @Test
    public void canParseSingleBooleanAttribute() {
        String json = "{ \"alive\": true }";
        Map<String, Object> tree = new JsonToMap().parse(json);

        assertThat(tree.size(), equalTo(1));
        assertThat(tree.get("alive"), equalTo(Boolean.TRUE));
    }
    @Test
    public void canParseSingleStringAttribute() {
        String json = "{ \"alive\": \"yes\" }";
        Map<String, Object> tree = new JsonToMap().parse(json);

        assertThat(tree.size(), equalTo(1));
        assertThat(tree.get("alive"), equalTo("yes"));
    }
    @Test
    public void preservesSpaceInData() {
        String json = "{ \"alive\": \"yes Sir\" }";
        Map<String, Object> tree = new JsonToMap().parse(json);

        assertThat(tree.size(), equalTo(1));
        assertThat(tree.get("alive"), equalTo("yes Sir"));
    }
    @Test
    public void preservesSpecialCharactersInData() {
        String json = "{ \"alive\": \"Indeed, because: I'm still young you! [{}}]\\\", trap: \\\"'$%\" }";
        Map<String, Object> tree = new JsonToMap().parse(json);

        assertThat(tree.size(), equalTo(1));
        assertThat(tree.get("alive"), equalTo("Indeed, because: I'm still young you! [{}}]\", trap: \"'$%"));
    }
    @Test
    public void canParseTwoAttributes() {
        String json = "{ \"alive\": true, \"status\": \"operational\" }";
        Map<String, Object> tree = new JsonToMap().parse(json);

        assertThat(tree.size(), equalTo(2));
        assertThat(tree.get("alive"), equalTo(Boolean.TRUE));
        assertThat(tree.get("status"), equalTo("operational"));
    }
    @Test
    public void canParseNestedObject() {
        String json = "{ \"report\": { \"old\": true, \"obsolete\": false }, \"status\": \"operational\" }";
        Map<String, Object> tree = new JsonToMap().parse(json);

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
        Map<String, Object> tree = new JsonToMap().parse(json);

        assertThat(tree.size(), equalTo(1));
        Map<String, Object> report = (Map<String, Object>) tree.get("report");
        assertThat(report.get("alive"), equalTo("Indeed, because: I'm still young you! [{}}]\", trap: \"'$%"));
    }
    @Test
    public void canParseSecondLevelNestedObject() {
        String json = "{ \"report\": { \"status\": { \"old\": true, \"obsolete\": false } } }";
        Map<String, Object> tree = new JsonToMap().parse(json);

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
        Map<String, Object> tree = new JsonToMap().parse(json);

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
        Map<String, Object> tree = new JsonToMap().parse(json);

        assertThat(tree.size(), equalTo(1));
        List<Object> people = (List<Object>) tree.get("people");
        assertThat(people.size(), equalTo(0));
    }
    @Test
    public void resistsEmptyObject() {
        String json = "{}";
        Map<String, Object> tree = new JsonToMap().parse(json);

        assertThat(tree.size(), equalTo(0));
    }
    @Test
    public void canParseNumbers() {
        String json = "{ \"one\": 42, \"two\": 15.09 }";
        Map<String, Object> tree = new JsonToMap().parse(json);

        assertThat(tree.size(), equalTo(2));
        assertThat(tree.get("one"), equalTo(new Integer(42)));
        assertThat(tree.get("two"), equalTo(new Double(15.09)));
    }
    @Test
    public void resistsIndentation() {
        String json = "{\n\t\"old\": true,\n\t\"obsolete\": false\n}";
        Map<String, Object> tree = new JsonToMap().parse(json);

        assertThat(tree.size(), equalTo(2));
        assertThat(tree.get("old"), equalTo(Boolean.TRUE));
        assertThat(tree.get("obsolete"), equalTo(Boolean.FALSE));
    }
    @Test
    public void resistsExtraSpaces() {
        String json = "{  \"obsolete\"   :  \"no\"  }";
        Map<String, Object> tree = new JsonToMap().parse(json);

        assertThat(tree.size(), equalTo(1));
        assertThat(tree.get("obsolete"), equalTo("no"));
    }
    @Test
    public void resistsNull() {
        String json = "{ \"attributes\": null }";
        Map<String, Object> tree = new JsonToMap().parse(json);

        assertThat(tree.size(), equalTo(1));
        assertThat(tree.get("attributes"), equalTo(null));
    }
    @Test
    public void resistsMalformedJson() {
        String json = "{ looks like json: \"but\" no :(";
        try{
            new JsonToMap().parse(json);
            fail();
        }
        catch(RuntimeException e) {
            e.printStackTrace();
            assertThat(e.getMessage(), equalTo("Malformed json? Input was: " + json));
        }
    }
}

class JsonToMap {

    public Map<String, Object> parse(String input) {
        try {
            String json = input.replaceAll("\\n|\\t", "");
            Map<String, Object> o = new HashMap();

            while (json.indexOf('\"') != -1) {
                String key = extractKey(json);
                String value = extractValue(key, json);
                o.put(key, parseValue(value));

                json = json.substring(json.indexOf(key) + key.length() + 1);
                json = json.substring(json.indexOf(value) + value.length() + 1);
            }
            return o;
        }
        catch (Exception e) {
            throw new RuntimeException("Malformed json? Input was: " + input);
        }
    }

    private List<Map<String, Object>> parseCollection(String input) {
        List<Map<String, Object>> collection = new ArrayList<>();

        while (input.indexOf('{') != -1) {
            String json = extractBetween('{', '}', input.substring(input.indexOf("{")));
            collection.add(parse(json));

            input = input.substring(input.indexOf(json) + json.length() + 1);
        }
        return collection;
    }

    private Object parseValue(String value) {
        if ("null".equalsIgnoreCase(value)) { return null; }
        if ("true".equalsIgnoreCase(value)) { return Boolean.TRUE; }
        if ("false".equalsIgnoreCase(value)) { return Boolean.FALSE; }
        if (value.startsWith("{")) { return parse(value); }
        if (value.startsWith("[")) { return parseCollection(value); }
        if (value.startsWith("\"")) { return value.substring(1, value.length()-1).replace("\\\"", "\""); }

        try {
            return new Integer(value);
        }
        catch (NumberFormatException e) {
            return new Double(value);
        }
    }

    private String extractKey(String json) {
        int start = json.indexOf("\"");
        String tail = json.substring(start + 1);
        int end = tail.indexOf("\"");

        return tail.substring(0, end);
    }

    private String extractValue(String key, String json) {
        json = json.substring(json.indexOf(key) + key.length() + 1).trim();
        json = json.substring(json.indexOf(":") + ":".length()).trim();
        if (json.startsWith("\"")) {
            boolean found = false;
            int index = 0;
            while (!found) {
                index += 1;
                if (isQuoteNotEscaped(index, json)) {
                    found = true;
                }
            }

            return '"' + json.substring(1, index) + '"';
        }
        else if (json.startsWith("{")) {
            return extractBetween('{', '}', json);
        }
        else if (json.startsWith("[")) {
            return extractBetween('[', ']', json);
        }
        int nextSpace = json.indexOf(" ");
        if (nextSpace == -1) { nextSpace = Integer.MAX_VALUE; }
        int nextComma = json.indexOf(",");
        if (nextComma == -1) { nextComma = Integer.MAX_VALUE; }
        int theEnd = json.indexOf("}");
        if (theEnd == -1) { theEnd = Integer.MAX_VALUE; }
        int end = Math.min(nextComma, nextSpace);
        end = Math.min(end, theEnd);
        return json.substring(0, end);
    }

    private String extractBetween(char opening, char closing, String input) {
        boolean found = false;
        int index = 0;
        int notEscapedQuoteCount = 0;
        int curlyBraketsCount = 1;
        while (!found) {
            index += 1;
            if (isQuoteNotEscaped(index, input)) {
                notEscapedQuoteCount = 1 - notEscapedQuoteCount;
            }
            char current = input.charAt(index);
            if (opening == current) {
                if (notEscapedQuoteCount == 0) {
                    curlyBraketsCount++;
                }
            }
            if (closing == current) {
                if (notEscapedQuoteCount == 0) {
                    curlyBraketsCount--;
                }
                if (curlyBraketsCount == 0) {
                    found = true;
                }
            }
        }
        return input.substring(0, index+1);
    }

    private boolean isQuoteNotEscaped(int index, String input) {
        char current = input.charAt(index);
        char previous = input.charAt(index-1);
        if ('\"' == current && !('\\' == previous)) {
            return true;
        }
        return false;
    }
}
