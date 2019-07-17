package ericminio.demo.primefactors.domain;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Mathematician {

    public Decomposition decompose(Integer number) {
        if (number > 10000) {
            throw new IllegalArgumentException("number <= 10000 expected");
        }

        Decomposition decomposition = new Decomposition();
        List<Integer> factors = new ArrayList<>();

        Integer candidate = 2;
        while (number > 1) {
            while (number % candidate == 0) {
                factors.add(candidate);
                number /= candidate;
            }
            candidate ++;
        }

        decomposition.setFactors(factors);
        return decomposition;

    }
}
