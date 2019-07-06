package ericminio.http.helloworld;

import ericminio.domain.helloworld.ExtractName;
import ericminio.domain.helloworld.Greeting;
import ericminio.domain.helloworld.BuildGreeting;
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