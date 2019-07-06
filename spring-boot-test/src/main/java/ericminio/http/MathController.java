package ericminio.http;

import ericminio.domain.Decomposition;
import ericminio.domain.Mathematician;
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

    @RequestMapping(value="/primeFactorsOf")
    public Decomposition primeFactorsOf(@RequestParam(value="number") Integer number) {
        return einstein.primeFactorsOf(number);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleMathematicianLimitations(IllegalArgumentException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_IMPLEMENTED);
    }
}