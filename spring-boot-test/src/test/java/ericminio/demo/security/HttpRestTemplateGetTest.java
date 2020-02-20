package ericminio.demo.security;

import ericminio.demo.helloworld.domain.Greeting;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= RANDOM_PORT)
public class HttpRestTemplateGetTest {

    @LocalServerPort
    int port;

    @Autowired
    RestTemplate restTemplate;

    private String greeting;

    @Before
    public void buildEndpoint() {
        greeting = "http://localhost:"+ port +"/greeting";
    }

    @Test
    public void needsValidCredentials() {
        TestRestTemplate restTemplate = new TestRestTemplate("user", "wrong-password");
        ResponseEntity<Greeting> response = restTemplate.getForEntity(greeting, Greeting.class);

        assertThat( response.getStatusCode(), equalTo( HttpStatus.UNAUTHORIZED ) );
    }

    @Test
    public void canAuthenticate() {
        ResponseEntity<Greeting> response = restTemplate.getForEntity(greeting, Greeting.class);

        assertThat( response.getStatusCode(), equalTo( HttpStatus.OK ) );
    }
}
