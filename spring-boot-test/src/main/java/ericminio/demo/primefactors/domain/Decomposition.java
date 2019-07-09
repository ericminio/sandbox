package ericminio.demo.primefactors.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Decomposition implements Serializable {

    private List<Integer> factors;

    public Decomposition() {
        this(new ArrayList<>());
    }

    public Decomposition(ArrayList<Integer> factors) {
        this.factors = factors;
    }

    public List<Integer> getFactors() {
        return factors;
    }

    public void setFactors(List<Integer> factors) {
        this.factors = factors;
    }
}
