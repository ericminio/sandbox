package ericminio.http;

import ericminio.domain.ExtractName;
import ericminio.domain.Greeting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";

    @Autowired
    private ExtractName extractName;

    @RequestMapping(value="/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
        Greeting data = new Greeting();
        data.content = String.format(template, name);

        return data;
    }

    @RequestMapping(method = POST, value="/greetings")
    public String greetings(@RequestBody Greeting greeting) {
        String name = extractName.please(greeting.content, template);

        return "My name is not " + name;
    }
}