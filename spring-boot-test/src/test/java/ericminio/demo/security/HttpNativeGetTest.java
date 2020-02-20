package ericminio.demo.security;

import ericminio.support.BasicHeaders;
import ericminio.support.HttpResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.HttpURLConnection;
import java.net.URL;

import static ericminio.support.GetRequest.get;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= RANDOM_PORT)
public class HttpNativeGetTest {

    @LocalServerPort
    int port;

    @Autowired
    BasicHeaders basic;

    private String greeting;

    @Before
    public void buildEndpoint() {
        greeting = "http://localhost:"+ port +"/greeting";
    }

    @Test
    public void needsValidCredentials() throws Exception {
        URL url = new URL(greeting);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        assertThat( connection.getResponseCode(), equalTo( 401 ) );
    }

    @Test
    public void canAuthenticate() throws Exception {
        HttpResponse response = get(greeting, basic.headers());

        assertThat( response.getStatusCode(), equalTo( 200 ) );
    }
}
