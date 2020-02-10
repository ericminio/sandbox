package ericminio.demo.primefactors.domain;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MathematicianComponentTest {

    @Autowired
    private Mathematician einstein;

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
