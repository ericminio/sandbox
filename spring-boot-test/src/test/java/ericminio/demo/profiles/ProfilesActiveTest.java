package ericminio.demo.profiles;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@SpringBootTest(
        properties= {
                "spring.profiles.active=one,two",

                "spring.profiles.active=last-active-wins"
        }
)
@RunWith(SpringRunner.class)
public class ProfilesActiveTest {

    @Autowired
    Environment environment;

    @Test
    public void areVisibleInEnvironment() {
        assertThat(environment.getActiveProfiles(), equalTo(new String[]{ "last-active-wins" }));
    }
}
