package ericminio.demo.filter;

import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class InterceptedController {

    @RequestMapping(value="/intercepted")
    Intercepted intercepted(@RequestHeader HttpHeaders headers) {
        Intercepted filtered = new Intercepted();
        List<String> values = headers.get("X-INTERCEPTED");
        String value = values != null ? values.get(0) : "NOT FOUND";
        filtered.setValue(value);

        return filtered;
    }
}