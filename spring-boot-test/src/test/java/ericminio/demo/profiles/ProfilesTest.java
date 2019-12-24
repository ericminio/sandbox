package ericminio.demo.profiles;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Java6Assertions.assertThat;

@SpringBootTest(
        properties= {
                "spring.profiles.active=visible"
        }
)
@RunWith(SpringRunner.class)
public class ProfilesTest {

    @Autowired
    Stuff stuff;

    @Test
    public void canBeUsedToActivateSpecificComponent() {
        assertThat(stuff.getName()).isEqualTo("good");
    }
}
