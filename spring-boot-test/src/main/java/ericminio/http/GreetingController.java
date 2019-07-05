package ericminio.http;

import ericminio.domain.ExtractName;
import ericminio.domain.Greeting;
import ericminio.domain.BuildGreeting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class GreetingController {

    @Autowired
    private BuildGreeting buildGreeting;

    @Autowired
    private ExtractName extractName;

    @RequestMapping(value="/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return buildGreeting.from(name).please();
    }

    @RequestMapping(method = POST, value="/greetings")
    public String greetings(@RequestBody Greeting greeting) {
        return "My name is not " + extractName.from(greeting).please();
    }
}