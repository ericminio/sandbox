package ericminio.json;

import java.util.HashMap;
import java.util.Map;

public class JsonToMapsParser {

    public static Map<String, Object> parse(String json) {
        json = json
                .replaceAll("\\s", "")
                .replaceAll("\\t", "")
        ;
        debug("->"+json);

        Map<String, Object> tree = new HashMap<>();
        while (! "{}".equalsIgnoreCase(json)) {
            String key = extractFirstKey(json);
            String value = extractFirstValue(json);
            debug(key);
            debug(value);
            tree.put(key, digest(value));

            json = removeFirstAttribute(json, key, value);
        }

        return tree;
    }

    private static Object digest(String value) {
        if (value.indexOf("{") == -1) { return clean(value); }
        return parse(value);
    }

    private static Object clean(String input) {
        if ("true".equalsIgnoreCase(input)) { return Boolean.TRUE; }
        if ("false".equalsIgnoreCase(input)) { return Boolean.FALSE; }
        if (input.startsWith("\"") && input.endsWith("\"")) {
            return input.substring(1, input.length()-1);
        }

        throw new RuntimeException("teach me what to do with: " + input);
    }

    private static String removeFirstAttribute(String json, String key, String value) {
        json = json.replaceFirst("\""+key+"\"", "");
        json = json.replaceFirst(":", "");
        json = json.replaceFirst(value.replace("{", "\\{").replace("}", "\\}"), "");
        json = json.replaceFirst(",", "");

        return json;
    }

    private static String extractFirstValue(String json) {
        int candidate = json.indexOf(":")+1;
        if (json.charAt(candidate) == '{') {
            int start = json.substring(1).indexOf("{") + 1;
            int nestedCount = 0;
            int cursor = start;
            boolean found = false;
            while (!found) {
                char current = json.charAt(cursor);
                if (current == '{') {
                    nestedCount++;
                }
                if (current == '}') {
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
        else {
            if (json.indexOf(",") != -1) {
                return json.substring(candidate, json.indexOf(","));
            }
            else {
                return json.substring(candidate, json.indexOf("}"));
            }
        }
    }

    private static String extractFirstKey(String json) {
        String before = json.substring(1, json.indexOf(":"));
        return (String) clean(before);
    }

    private static void debug(String message) {
        System.out.println(message);
    }
}
