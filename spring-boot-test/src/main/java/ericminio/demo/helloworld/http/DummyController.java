package ericminio.demo.helloworld.http;

import ericminio.demo.helloworld.domain.Greeting;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface DummyController {

    @RequestMapping(value="/greeting")
    default Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return null;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    default ResponseEntity<String> guard(IllegalArgumentException exception) {
        return new ResponseEntity<>(
                exception.getMessage() != null ? exception.getMessage() : exception.getClass().getSimpleName() ,
                HttpStatus.NOT_IMPLEMENTED);
    }
}