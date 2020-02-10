package ericminio.support;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Configuration
public class ValidCredentials {

    @Value("${custom.security.username}")
    private String username;

    @Value("${custom.security.password}")
    private String password;

    @Bean
    public RestTemplate getRestTemplateWithValidCredentials() {
        RestTemplate restTemplate = new RestTemplate();

        List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors();
        if (interceptors == null) {
            interceptors = Collections.emptyList();
        }

        interceptors = new ArrayList(interceptors);
        BasicAuthenticationInterceptor.class.getClass();
        interceptors.removeIf(BasicAuthenticationInterceptor.class::isInstance);
        interceptors.add(new BasicAuthenticationInterceptor(username, password));
        restTemplate.setInterceptors(interceptors);

        return restTemplate;
    }
}
