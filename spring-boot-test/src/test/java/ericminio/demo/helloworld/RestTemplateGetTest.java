package ericminio.demo.helloworld;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ericminio.domain.BuildGreeting;
import ericminio.domain.Greeting;
import ericminio.support.HttpResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static ericminio.support.GetRequest.get;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= RANDOM_PORT)
public class RestTemplateGetTest {

    @LocalServerPort
    int port;

    private String greeting;

    @Before
    public void buildEndpoint() {
        greeting = "http://localhost:"+ port +"/greeting";
    }

    @Test
    public void canAccessEndpoint() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Greeting> response = restTemplate.getForEntity(greeting, Greeting.class);

        assertThat( response.getStatusCode(), equalTo( HttpStatus.OK ) );
    }

    @Test
    public void canReadHeadersViaEntityRequest() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Greeting> response = restTemplate.getForEntity(greeting, Greeting.class);

        assertThat( response.getHeaders().getContentType(), equalTo(APPLICATION_JSON_UTF8 ) );
    }

    @Test
    public void hasSpecialApiForHeaders() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = restTemplate.headForHeaders(greeting);

        assertThat( headers.getContentType(), equalTo(APPLICATION_JSON_UTF8 ) );
    }

    @Test
    public void canReadBodyAsEntity() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Greeting> response = restTemplate.getForEntity(greeting + "?name=Joe", Greeting.class);

        assertThat( response.getBody(), equalTo( new BuildGreeting().from("Joe").please() ) );
    }

    @Test
    public void canReadBodyAsString() throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(greeting + "?name=Joe", String.class);
        Greeting actual = new ObjectMapper().readValue(response.getBody(), Greeting.class);

        assertThat( actual, equalTo( new BuildGreeting().from("Joe").please() ) );
    }
}
