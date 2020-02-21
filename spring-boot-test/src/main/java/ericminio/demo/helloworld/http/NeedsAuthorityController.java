package ericminio.demo.helloworld.http;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize("hasAuthority('ADMIN')")
public class NeedsAuthorityController {

    @RequestMapping(value="/needs-authority")
    public String greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return "Hello ADMIN";
    }
}