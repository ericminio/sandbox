package ericminio.demo.configuration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {
    "spring.main.allow-bean-definition-overriding=true"
})
public class BeanOverrideTest {

    @TestConfiguration
    public static class ConfigurationProvidingTestBean {

        @Bean
        public AnyBean theAnyBeanWeNeed() {
            return new AnyBeanTestingFriendly();
        }
    }

    @Autowired
    AnyBean anyBean;

    @Test
    public void instance() {
        assertThat(anyBean, instanceOf(AnyBeanTestingFriendly.class));
    }
}
