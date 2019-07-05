package ericminio.http;

import ericminio.support.HttpResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static ericminio.support.PostRequest.post;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= RANDOM_PORT)
public class HttpPostTest {

    @LocalServerPort
    int port;

    private String endpoint;

    @Before
    public void buildEndpoint() {
        endpoint = "http://localhost:"+ port +"/greetings";
    }

    @Test
    public void works() throws Exception {
        String message = "{\"content\":\"Hello, Hal!\"}";
        HttpResponse response = post(endpoint, message.getBytes());

        assertThat( response.getBody(), equalTo( "My name is not Hal" ) );
    }
}
