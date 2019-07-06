package ericminio.domain;

import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class Mathematician {

    public Decomposition primeFactorsOf(Integer number) {
        ArrayList<Integer> factors = new ArrayList<>();
        Integer candidate = 2;
        while (number > 1) {
            while (number % candidate == 0) {
                factors.add(candidate);
                number /= candidate;
            }
            candidate ++;
        }

        Decomposition decomposition = new Decomposition();
        decomposition.factors = factors;
        return decomposition;
    }
}
