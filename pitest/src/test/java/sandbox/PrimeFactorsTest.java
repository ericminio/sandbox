package sandbox;

import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static sandbox.Einstein.primeFactorsOf;

public class PrimeFactorsTest {

    @Test
    public void canDecomposeTwo() {
        assertThat(primeFactorsOf(2), equalTo(Arrays.asList(2)));
    }

    @Test
    public void canDecompose64() {
        assertThat(primeFactorsOf(64), equalTo(Arrays.asList(2, 2, 2, 2, 2, 2)));
    }
}
