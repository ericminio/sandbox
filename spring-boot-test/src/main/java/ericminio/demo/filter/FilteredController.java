package ericminio.demo.filter;

import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FilteredController {

    @RequestMapping(value="/filtered")
    Filtered filtered(@RequestHeader HttpHeaders headers) {
        Filtered filtered = new Filtered();
        List<String> values = headers.get("X-HEADER");
        String value = values != null ? values.get(0) : "NOT FOUND";
        filtered.setValue(value);

        return filtered;
    }
}