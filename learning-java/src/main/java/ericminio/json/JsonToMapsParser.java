package ericminio.json;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonToMapsParser {

    public static Map<String, Object> parse(String json) {
        try {
            json = json
                    .replaceAll("\\n", "")
                    .replaceAll("\\t", "")
                    .replaceAll("\\{\\s*", "{")
                    .replaceAll("\\[\\s*", "[")
                    .replaceAll(":\\s*", ":")
                    .replaceAll("\\s*:", ":")
                    .replaceAll(",\\s*", ",")
                    .replaceAll("\\s*\\]", "]")
                    .replaceAll("\\s*\\}", "}")
            ;
            debug("->" + json);

            return parseObject(json);
        }
        catch (Exception any) {
            throw new RuntimeException("Malformed json?", any);
        }
    }

    private static Map<String, Object> parseObject(String json) {
        Map<String, Object> tree = new HashMap<>();
        while (!"{}".equalsIgnoreCase(json)) {
            String key = extractFirstKey(json);
            String value = extractFirstValue(json);
            debug(key);
            debug(value);
            tree.put(key, digest(value));

            json = removeFirstAttribute(json, key, value);
        }

        return tree;
    }

    private static List<Map<String, Object>> parseCollection(String value) {
        List<Map<String, Object>> collection = new ArrayList<>();
        while (! "[]".equalsIgnoreCase(value)) {
            String item = extractItem(value, 1, '{', '}');
            collection.add(parseObject(item));
            value = removeFirstItem(value, item);
        }
        return collection;
    }

    private static Object digest(String value) {
        if (value.indexOf("{") == -1) { return clean(value); }
        if (value.indexOf("[") == 0) { return parseCollection(value); }
        return parseObject(value);
    }

    private static Object clean(String input) {
        if ("true".equalsIgnoreCase(input)) { return Boolean.TRUE; }
        if ("false".equalsIgnoreCase(input)) { return Boolean.FALSE; }
        if (input.startsWith("\"") && input.endsWith("\"")) {
            return input.substring(1, input.length()-1);
        }
        try {
            return new Integer(input);
        }
        catch (NumberFormatException e) {}
        try {
            return new BigDecimal(input);
        }
        catch (NumberFormatException e) {}

        throw new RuntimeException("teach me what to do with:" + input);
    }

    private static String removeFirstAttribute(String json, String key, String value) {
        json = removeKey(json, key);
        json = json.replaceFirst(":", "");
        json = removeFirstItem(json, value);

        return json;
    }

    private static String removeValue(String json, String value) {
        return json.replaceFirst(value
                        .replace("{", "\\{")
                        .replace("}", "\\}")
                        .replace("[", "\\[")
                        .replace("]", "\\]")
                , "");
    }

    private static String removeFirstItem(String value, String item) {
        value = removeValue(value, item);
        value = value.replaceFirst(",", "");

        return value;
    }

    private static String removeKey(String json, String key) {
        return json.replaceFirst("\""+key+"\"", "");
    }

    private static String extractFirstValue(String json) {
        int candidate = json.indexOf(":")+1;

        if (json.charAt(candidate) == '{') {
            return extractItem(json, candidate, '{', '}');
        }
        else if (json.charAt(candidate) == '[') {
            return extractItem(json, candidate, '[', ']');
        }
        else {
            if (json.indexOf(",") != -1) {
                return json.substring(candidate, json.indexOf(","));
            }
            else {
                return json.substring(candidate, json.indexOf("}"));
            }
        }
    }

    private static String extractItem(String json, int start, char opening, char closing) {
        int nestedCount = 0;
        int cursor = start;
        boolean found = false;
        while (!found) {
            char current = json.charAt(cursor);
            if (current == opening) {
                nestedCount++;
            }
            if (current == closing) {
                nestedCount--;
                if (nestedCount == 0) {
                    found = true;
                }
            }
            if (!found) {
                cursor++;
            }
        }
        return json.substring(start, cursor + 1);
    }

    private static String extractFirstKey(String json) {
        String before = json.substring(1, json.indexOf(":"));
        return (String) clean(before);
    }

    private static void debug(String message) {
        //System.out.println(message);
    }
}
