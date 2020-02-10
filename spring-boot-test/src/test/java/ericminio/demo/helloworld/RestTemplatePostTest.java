package ericminio.demo.helloworld;

import ericminio.demo.helloworld.domain.BuildGreeting;
import ericminio.demo.helloworld.domain.Greeting;
import ericminio.support.Csrf;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpMethod.POST;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= RANDOM_PORT)
public class RestTemplatePostTest {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    Csrf csrf;

    @LocalServerPort
    int port;

    private String greetings;

    @Before
    public void buildEndpoint() {
        greetings = "http://localhost:"+ port +"/greetings";
    }

    @Test
    public void works() {
        HttpEntity<Greeting> request = new HttpEntity<>(new BuildGreeting().from("Hal").please(), csrf.headers());
        ResponseEntity<String> response = restTemplate.exchange(greetings, POST, request, String.class);

        assertThat( response.getStatusCode(), equalTo( HttpStatus.OK ) );
        assertThat( response.getBody(), equalTo( "My name is not Hal" ) );
    }
}
