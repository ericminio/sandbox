package ericminio.demo.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigurationProvidingProductionBean {

    @Bean
    public AnyBean theAnyBeanWeNeed() {
        return new AnyBean();
    }
}
