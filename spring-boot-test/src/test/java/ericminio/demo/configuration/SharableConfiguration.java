package ericminio.demo.configuration;

import ericminio.domain.configuration.Something;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by perso on 2017-06-22.
 */
@Configuration
public class SharableConfiguration {

    @Bean
    public Something buildsOneSpecificInstance() {
        return new Something("shared");
    }
}
