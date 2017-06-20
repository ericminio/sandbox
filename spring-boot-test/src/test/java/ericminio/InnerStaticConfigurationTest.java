package ericminio;

import ericminio.domain.Something;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(SpringRunner.class)
public class InnerStaticConfigurationTest {

    @Autowired
    Something something;

    @Test
    public void configurationCanInjectNativeBean() {
        assertThat(something).isNotNull();
    }

    @Configuration
    static class AutowiredBeanFactory {

        @Bean
        public Something knowsHowToBuildSomething() {
            return new Something();
        }
    }
}
