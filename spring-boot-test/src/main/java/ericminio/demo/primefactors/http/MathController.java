package ericminio.demo.primefactors.http;

import ericminio.demo.primefactors.domain.Decomposition;
import ericminio.demo.primefactors.domain.Mathematician;
import ericminio.demo.primefactors.security.TooManyRequestException;
import ericminio.demo.primefactors.security.TrafficLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MathController {

    @Autowired
    Mathematician einstein;

    @Autowired
    TrafficLimiter trafficLimiter;

    @RequestMapping("/primeFactorsOf")
    public Decomposition primeFactorsOf(@RequestParam Integer number) {
        if (trafficLimiter.isLimitReachedFor(number)) {
            throw new TooManyRequestException();
        }
        return einstein.decompose(number);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleMathematicianLimitations(IllegalArgumentException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_IMPLEMENTED);
    }

    @ExceptionHandler(TooManyRequestException.class)
    public ResponseEntity<String> handleNotManyRequestException(TooManyRequestException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.TOO_MANY_REQUESTS);
    }

}
