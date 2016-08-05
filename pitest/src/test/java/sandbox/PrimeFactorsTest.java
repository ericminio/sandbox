package sandbox;

import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static sandbox.Einstein.primeFactorsOf;

public class PrimeFactorsTest {

    @Test
    public void canDecomposeFour() {
        assertThat(primeFactorsOf(4), equalTo(Arrays.asList(2, 2)));
    }
}
