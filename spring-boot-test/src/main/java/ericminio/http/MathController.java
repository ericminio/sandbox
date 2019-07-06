package ericminio.http;

import ericminio.domain.Decomposition;
import ericminio.domain.Mathematician;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MathController {

    @Autowired
    Mathematician einstein;

    @RequestMapping(value="/primeFactorsOf")
    public ResponseEntity<Decomposition> primeFactorsOf(@RequestParam(value="number") Integer number) {
        if (number > 10000) {
            return new ResponseEntity<>(new Decomposition(), HttpStatus.NOT_IMPLEMENTED);
        }
        return new ResponseEntity<>(einstein.primeFactorsOf(number), HttpStatus.OK);
    }
}