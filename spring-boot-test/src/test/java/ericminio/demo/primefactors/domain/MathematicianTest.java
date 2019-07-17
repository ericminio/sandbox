package ericminio.demo.primefactors.domain;

import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class MathematicianTest {

    private Mathematician einstein = new Mathematician();

    @Test
    public void canDecompose2() {
        assertThat(einstein.decompose(2).getFactors(), equalTo(Arrays.asList(2)));
    }
    @Test
    public void canDecompose4() {
        assertThat(einstein.decompose(4).getFactors(), equalTo(Arrays.asList(2, 2)));
    }
    @Test
    public void canDecompose12() {
        assertThat(einstein.decompose(12).getFactors(), equalTo(Arrays.asList(2, 2, 3)));
    }
}
