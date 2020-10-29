package ericminio.json;

import java.util.*;

public class JsonToMapsParser {

    private void debug(String json) {
        //System.out.println(json);
    }

    public Object parse(String value) {
        if ("null".equalsIgnoreCase(value)) { return null; }
        if ("true".equalsIgnoreCase(value)) { return Boolean.TRUE; }
        if ("false".equalsIgnoreCase(value)) { return Boolean.FALSE; }
        if (value.startsWith("{")) { return parseObject(value); }
        if (value.startsWith("[")) { return parseCollection(value); }
        if (value.startsWith("\"")) { return value.substring(1, value.length()-1).replace("\\\"", "\""); }

        try {
            return Integer.valueOf(value);
        }
        catch (NumberFormatException e) {
            return Double.valueOf(value);
        }
    }

    public Map<String, Object> parseObject(String input) {
        try {
            String json = input.replaceAll("\\n|\\t", "");
            Map<String, Object> o = new HashMap();
            debug("-> " + json);

            while (json.indexOf('\"') != -1) {
                String key = extractKey(json);
                String value = extractValue(key, json);
                debug("key: " + key);
                debug("value: " + value);
                o.put(key, parse(value));

                json = json.substring(json.indexOf(key) + key.length() + 1);
                json = json.substring(json.indexOf(value) + value.length() + 1);
            }
            return o;
        }
        catch (Exception e) {
            throw new RuntimeException("Malformed json? Input was: " + input);
        }
    }

    public List<Object> parseCollection(String input) {
        List<Object> collection = new ArrayList<>();
        input = input.substring(1, input.length()-1);

        while (input.indexOf('{') != -1) {
            String json = extractBetween('{', '}', input.substring(input.indexOf("{")));
            collection.add(parse(json));

            input = input.substring(input.indexOf(json) + json.length());
        }

        String[] splited = input.split(",");
        for (String item: splited) {
            item = item.trim();
            if (item.length() > 0) {
                collection.add(parse(item));
            }
        }

        return collection;
    }

    private String extractKey(String json) {
        int start = json.indexOf("\"");
        String tail = json.substring(start + 1);
        int end = tail.indexOf("\"");

        return tail.substring(0, end);
    }

    private String extractValue(String key, String json) {
        json = json.substring(json.indexOf(key) + key.length() + 1).trim();
        json = json.substring(json.indexOf(":") + 1).trim();
        if (json.startsWith("\"")) {
            return extractFirstString(json);
        }
        else if (json.startsWith("{")) {
            return extractBetween('{', '}', json);
        }
        else if (json.startsWith("[")) {
            return extractBetween('[', ']', json);
        }
        else {
            return extractNumberOrBoolean(json);
        }
    }

    private String extractNumberOrBoolean(String json) {
        int nextSpace = json.indexOf(" ") != -1 ? json.indexOf(" "): Integer.MAX_VALUE;
        int nextComma = json.indexOf(",") != -1 ? json.indexOf(","): Integer.MAX_VALUE;
        int theEnd = json.indexOf("}") != -1 ? json.indexOf("}"): Integer.MAX_VALUE;

        return json.substring(0, Math.min(Math.min(nextComma, nextSpace), theEnd));
    }

    private String extractFirstString(String input) {
        boolean found = false;
        int index = 0;
        while (!found) {
            index += 1;
            if (isQuoteNotEscaped(index, input)) {
                found = true;
            }
        }

        return '"' + input.substring(1, index) + '"';
    }

    private String extractBetween(char opening, char closing, String input) {
        boolean found = false;
        int index = 0;
        int notEscapedQuoteCount = 0;
        int openingAndClosingCount = 1;
        while (!found) {
            index += 1;
            if (isQuoteNotEscaped(index, input)) {
                notEscapedQuoteCount = 1 - notEscapedQuoteCount;
            }
            char current = input.charAt(index);
            if (opening == current) {
                if (notEscapedQuoteCount == 0) {
                    openingAndClosingCount++;
                }
            }
            if (closing == current) {
                if (notEscapedQuoteCount == 0) {
                    openingAndClosingCount--;
                }
                if (openingAndClosingCount == 0) {
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
