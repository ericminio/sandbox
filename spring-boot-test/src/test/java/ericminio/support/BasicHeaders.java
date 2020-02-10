package ericminio.support;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class BasicHeaders {

    public Map<String, String> headers() {
        Map<String, String> values = new HashMap<>();

        values.put("Authorization", "Basic " + Base64.encodeBase64String("user:correct-password".getBytes()));

        return values;
    }
}
