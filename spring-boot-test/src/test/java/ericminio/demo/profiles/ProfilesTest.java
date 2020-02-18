package ericminio.demo.profiles;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

@SpringBootTest(
        properties= {
                "spring.profiles.active=visible"
        }
)
@RunWith(SpringRunner.class)
public class ProfilesTest {

    @Autowired
    Stuff stuff;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Autowired
    ApplicationContext applicationContext;

    @Test
    public void activeProfileIsDisclosed() {
        assertThat(activeProfile, equalTo("visible"));
    }

    @Test
    public void canBeUsedToActivateSpecificComponent() {
        assertThat(stuff.getName(), equalTo("visible name"));
    }

    @Test
    public void impactsAvailableBeans() {
        assertThat(new ShouldBeUsed(), is(instanceOf(Stuff.class)));
        assertThat(new ShouldNotBeUsed(), is(instanceOf(Stuff.class)));
        Map<String, Stuff> stuffs = applicationContext.getBeansOfType(Stuff.class);

        assertThat(stuffs.size(), equalTo(1));

        String first = stuffs.keySet().iterator().next();
        assertThat(stuffs.get(first), is(instanceOf(ShouldBeUsed.class)));
    }
}
