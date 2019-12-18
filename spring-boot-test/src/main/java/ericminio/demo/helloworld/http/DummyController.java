package ericminio.demo.helloworld.http;

import ericminio.demo.helloworld.domain.BuildGreeting;
import ericminio.demo.helloworld.domain.ExtractName;
import ericminio.demo.helloworld.domain.Greeting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

public interface DummyController {

    @RequestMapping(value="/greeting")
    default Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return null;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    default ResponseEntity<String> guard(IllegalArgumentException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_IMPLEMENTED);
    }
}