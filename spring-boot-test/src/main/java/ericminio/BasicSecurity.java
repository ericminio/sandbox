package ericminio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Profile("!unsecure")
public class BasicSecurity extends WebSecurityConfigurerAdapter {

    @Value("${custom.security.username}")
    private String username;

    @Value("${custom.security.password}")
    private String password;

    @Value("${custom.security.adminUsername}")
    private String adminUsername;

    @Value("${custom.security.adminPassword}")
    private String adminPassword;

    @Autowired
    CsrfTokenRepository csrfTokenRepository;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        auth
                .inMemoryAuthentication()
                    .withUser(username)
                    .password(encoder.encode(password))
                    .authorities("USER")

                .and()
                    .withUser(adminUsername)
                    .password(encoder.encode(adminPassword))
                    .authorities("ADMIN");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .anyRequest()
                    .authenticated()
                .and()
                    .httpBasic()
                .and()
                    .csrf()
                    .csrfTokenRepository(csrfTokenRepository);
    }

}


