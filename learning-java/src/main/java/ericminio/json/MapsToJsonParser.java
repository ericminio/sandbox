package ericminio.json;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MapsToJsonParser {

    public static String stringify(Object o) {
        if (o == Boolean.TRUE) { return "true"; }
        if (o == Boolean.FALSE) { return "false"; }
        if (o instanceof String) { return "\"" + o + "\""; }
        if (o instanceof List) { return stringifyCollection((List) o); }
        if (! (o instanceof Map)) { return o.toString(); }

        Map<String, Object> tree = (Map) o;
        List<String> fields = new ArrayList<>();
        Iterator<String> keys = tree.keySet().iterator();
        while(keys.hasNext()) {
            String key = keys.next();
            Object value = tree.get(key);
            fields.add("\"" + key + "\":" + stringify(value));
        }

        return "{" + fields.stream().collect(Collectors.joining(",")) + "}";
    }

    private static String stringifyCollection(List list) {
        return "[" + list.stream().map(e -> stringify(e)).collect(Collectors.joining(",")) + "]";
    }
}
