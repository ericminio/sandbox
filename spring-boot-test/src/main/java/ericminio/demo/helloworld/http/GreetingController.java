package ericminio.demo.helloworld.http;

import ericminio.demo.helloworld.domain.ExtractName;
import ericminio.demo.helloworld.domain.Greeting;
import ericminio.demo.helloworld.domain.BuildGreeting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class GreetingController {

    @Autowired
    private BuildGreeting buildGreeting;

    @Autowired
    private ExtractName extractName;

    @RequestMapping(value="/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
        if (name.equalsIgnoreCase("XXX")) {
            throw new IllegalArgumentException();
        }
        return buildGreeting.from(name).please();
    }

    @RequestMapping(method = POST, value="/greetings")
    public String greetings(@RequestBody Greeting greeting) {
        return "My name is not " + extractName.from(greeting).please();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> guard(IllegalArgumentException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_IMPLEMENTED);
    }
}