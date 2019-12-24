package ericminio.demo.profiles;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@SpringBootTest(
        properties= {
                "spring.profiles.active=summer,joy"
        }
)
@RunWith(SpringRunner.class)
public class VariablesTest {

    @Value("${use.this}")
    private String summer;

    @Value("${use.this.too}")
    private String joy;

    @Test
    public void canBeInjectedFromMainContext() {
        assertThat(summer, equalTo("hello-world"));
    }

    @Test
    public void canBeInjectedFromTestContext() {
        assertThat(joy, equalTo("hello-spring"));
    }
}
