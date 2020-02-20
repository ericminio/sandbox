package ericminio.demo.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import ericminio.demo.helloworld.domain.BuildGreeting;
import ericminio.demo.helloworld.domain.Greeting;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.NOT_IMPLEMENTED;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= RANDOM_PORT)
public class RestTemplateGetTest {

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
    public void canAccessEndpoint() {
        ResponseEntity<Greeting> response = restTemplate.getForEntity(greeting, Greeting.class);

        assertThat( response.getStatusCode(), equalTo( HttpStatus.OK ) );
    }

    @Test
    public void canReadHeadersViaEntityRequest() {
        ResponseEntity<Greeting> response = restTemplate.getForEntity(greeting, Greeting.class);

        assertThat( response.getHeaders().getContentType(), equalTo(APPLICATION_JSON_UTF8 ) );
    }

    @Test
    public void hasSpecialApiForHeaders() {
        HttpHeaders headers = restTemplate.headForHeaders(greeting);

        assertThat( headers.getContentType(), equalTo(APPLICATION_JSON_UTF8 ) );
    }

    @Test
    public void canReadBodyAsEntity() {
        ResponseEntity<Greeting> response = restTemplate.getForEntity(greeting + "?name=Joe", Greeting.class);

        assertThat( response.getBody(), equalTo( new BuildGreeting().from("Joe").please() ) );
    }

    @Test
    public void canReadBodyAsString() throws IOException {
        ResponseEntity<String> response = restTemplate.getForEntity(greeting + "?name=Joe", String.class);
        Greeting actual = new ObjectMapper().readValue(response.getBody(), Greeting.class);

        assertThat( actual, equalTo( new BuildGreeting().from("Joe").please() ) );
    }

    @Test
    public void canHandleErrors() {
        restTemplate.setErrorHandler(new ResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse clientHttpResponse) {
                return false;
            }
            @Override
            public void handleError(ClientHttpResponse clientHttpResponse) {
            }
        });
        ResponseEntity<String> response = restTemplate.exchange(greeting + "?name=XXX", GET, null, String.class);

        assertThat(response.getStatusCode(), equalTo(NOT_IMPLEMENTED));
    }
}
