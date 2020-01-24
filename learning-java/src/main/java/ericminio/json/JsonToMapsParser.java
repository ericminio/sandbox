package ericminio.json;

import java.util.HashMap;
import java.util.Map;

public class JsonToMapsParser {

    public static Map<String, Object> parse(String json) {
        json = json
                .replaceAll("\\s", "")
                .replaceAll("\\t", "")
        ;
        if (json.lastIndexOf("{") == 0) { return parseSimpleObject(json); }

        debug("->"+json);
        Map<String, Object> tree = new HashMap<>();
        while (! "{}".equalsIgnoreCase(json)) {
            String key = extractFirstKey(json);
            String value = extractFirstValue(json);
            debug(key);
            debug(value);
            if (value.indexOf("{") == 0) {
                tree.put(key, parse(value));
            }
            else {
                tree.put(key, clean(value));
            }

            json = removeFirstAttribute(json, key, value);
        }

        return tree;
    }

    private static String removeFirstAttribute(String json, String key, String value) {
        json = json.replaceFirst("\""+key+"\"", "");
        json = json.replaceFirst(":", "");
        json = json.replaceFirst(value.replace("{", "\\{").replace("}", "\\}"), "");
        json = json.replaceFirst(",", "");

        return json;
    }

    private static String extractFirstValue(String json) {
        int comma = json.indexOf(":");
        if (json.charAt(comma+1) == '{') {
            int start = json.substring(1).indexOf("{") + 1;
            int nestedCount = 0;
            int cursor = start;
            boolean found = false;
            while (!found && cursor < json.length()) {
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
            return json.substring(comma+1, json.indexOf(","));
        }
    }

    private static String extractFirstKey(String json) {
        String before = json.substring(1, json.indexOf(":"));
        return (String) clean(before);
    }

    public static Map<String, Object> parseSimpleObject(String json) {
        Map<String, Object> o = new HashMap<>();
        json = json.substring(1).substring(0, json.length()-2);

        String[] pairs = json.split(",");
        for (int i = 0; i < pairs.length; i++) {
            String[] parts = pairs[i].split(":");
            String key = parts[0];
            String value = parts[1];
            o.put((String) clean(key), clean(value));
        }

        return o;
    }

    private static Object clean(String input) {
        if ("true".equalsIgnoreCase(input)) { return Boolean.TRUE; }
        if ("false".equalsIgnoreCase(input)) { return Boolean.FALSE; }
        if (input.startsWith("\"") && input.endsWith("\"")) {
            return input.substring(1, input.length()-1);
        }

        throw new RuntimeException("teach me what to do with: " + input);
    }

    private static void debug(String message) {
        System.out.println(message);
    }
}
