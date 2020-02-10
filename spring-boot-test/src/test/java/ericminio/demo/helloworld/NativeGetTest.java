package ericminio.demo.helloworld;

import com.fasterxml.jackson.databind.ObjectMapper;
import ericminio.demo.helloworld.domain.Greeting;
import ericminio.support.HttpResponse;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static ericminio.support.GetRequest.get;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= RANDOM_PORT)
public class NativeGetTest {

    @LocalServerPort
    int port;

    private String greeting;

    @Before
    public void buildEndpoint() {
        greeting = "http://localhost:"+ port +"/greeting";
    }

    @Test
    public void canAccessEndpoint() throws Exception {
        URL url = new URL(greeting);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        assertThat( connection.getResponseCode(), equalTo( 401 ) );
    }

    @Test
    public void canAuthenticate() throws Exception {
        HttpResponse response = get(greeting, headers());

        assertThat( response.getStatusCode(), equalTo( 200 ) );
    }

    private Map<String, String> headers() {
        Map<String, String> values = new HashMap<>();

        values.put("Authorization", "Basic " + Base64.encodeBase64String("user:correct-password".getBytes()));

        return values;
    }

    @Test
    public void canReadBodyAsJson() throws Exception {
        HttpResponse response = get( greeting + "?name=Joe", headers() );

        assertThat( response.getBody(), equalTo( "{\"content\":\"Hello, Joe!\"}" ) );
    }

    @Test
    public void btwEndpointResistsNoParameter() throws Exception {
        HttpResponse response = get(greeting, headers());

        assertThat( response.getBody(), equalTo( "{\"content\":\"Hello, World!\"}" ) );
    }

    @Test
    public void jsonSupportOfferedByJackson() throws Exception {
        HttpResponse response = get(greeting, headers());
        Greeting value = new ObjectMapper().readValue(response.getBody(), Greeting.class);

        assertThat( value.content, equalTo( "Hello, World!" ) );
    }
}
