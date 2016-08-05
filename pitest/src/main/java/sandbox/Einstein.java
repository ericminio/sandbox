package sandbox;

import java.util.ArrayList;
import java.util.List;

public class Einstein {

    public static List<Integer> primeFactorsOf(Integer number) {
        ArrayList<Integer> factors = new ArrayList<Integer>();

        Integer candidate = 2;
        while (number > 1) {
            if (number % candidate == 0) {
                factors.add(candidate);
                number /= candidate;
            }
        }

        return factors;
    }
}
