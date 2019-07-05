package ericminio.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import ericminio.domain.Greeting;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";

    @RequestMapping(value="/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
        Greeting data = new Greeting();
        data.content = String.format(template, name);

        return data;
    }

    @RequestMapping(method = POST, value="/greetings")
    public String greetings(@RequestBody Greeting greeting) {
        Pattern pattern = Pattern.compile("Hello,\\s(.*?)!");
        Matcher matcher = pattern.matcher(greeting.content);
        matcher.find();
        String name = matcher.group(1);

        return "My name is not " + name;
    }
}