package ericminio.demo.logging;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static ericminio.support.FileContent.contentOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

@SpringBootTest(
        properties= {
                "spring.profiles.active=peace"
        }
)
@RunWith(SpringRunner.class)
public class RootLevelTest {

    @Test
    public void worksWellAsCatchAll() throws Exception {
        assertThat(contentOf("logs/default.log"), containsString("The following profiles are active: peace"));
    }
}
