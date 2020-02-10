package ericminio.support;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class BasicHeaders {

    @Value("${custom.security.username}")
    private String username;

    @Value("${custom.security.password}")
    private String password;

    public Map<String, String> headers() {
        Map<String, String> values = new HashMap<>();

        values.put("Authorization", "Basic " + Base64.encodeBase64String((username + ":" + password).getBytes()));

        return values;
    }
}
