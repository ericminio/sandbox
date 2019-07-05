package ericminio.configuration;

import ericminio.domain.Something;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {SharableConfiguration.class})
public class ContextConfigurationWinsAgainstInnerClassTest {

    @Autowired
    Something something;

    @Test
    public void configurationCanInjectNativeBean() {
        assertThat(something).isNotNull();
    }

    @Test
    public void injectedInstanceIsTheExpectedOne() {
        assertThat(something.getName()).isEqualTo("shared");
    }

    @Configuration
    static class AutowiredBeanFactory {

        @Bean
        public Something knowsHowToBuildSomething() {
            return new Something("inner");
        }
    }
}
