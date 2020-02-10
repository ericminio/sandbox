package ericminio.demo.http;

import ericminio.support.CsrfHeaders;
import ericminio.support.HttpResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static ericminio.support.PostRequest.post;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= RANDOM_PORT)
public class NativePostTest {

    @Autowired
    CsrfHeaders csrf;

    @LocalServerPort
    int port;

    private String greetings;

    @Before
    public void buildEndpoint() {
        greetings = "http://localhost:"+ port +"/greetings";
    }

    @Test
    public void works() throws Exception {
        String message = "{\"content\":\"Hello, Hal!\"}";
        HttpResponse response = post(greetings, csrf.headers(), message.getBytes());

        assertThat( response.getStatusCode(), equalTo( 200 ) );
        assertThat( response.getBody(), equalTo( "My name is not Hal" ) );
    }
}
