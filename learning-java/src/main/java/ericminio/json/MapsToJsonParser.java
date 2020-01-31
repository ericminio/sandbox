package ericminio.json;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MapsToJsonParser {

    public static String stringify(Object o) {
        if (o == Boolean.TRUE) { return "true"; }
        if (o == Boolean.FALSE) { return "false"; }
        if (o instanceof String) { return quote((String) o); }
        if (o instanceof List) { return stringifyCollection((List) o); }

        if (! (o instanceof Map)) { return o.toString(); }

        List<String> fields = new ArrayList<>();
        Map<String, Object> map = (Map) o;
        map.forEach((key, value)-> {
            fields.add(quote(key) + ":" + stringify(value));
        });

        return "{" + fields.stream().collect(Collectors.joining(",")) + "}";
    }

    private static String stringifyCollection(List list) {
        return "[" + list.stream().map(e -> stringify(e)).collect(Collectors.joining(",")) + "]";
    }

    private static String quote(String key) {
        return "\"" + key + "\"";
    }
}
