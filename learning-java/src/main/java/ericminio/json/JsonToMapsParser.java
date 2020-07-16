package ericminio.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonToMapsParser {

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
