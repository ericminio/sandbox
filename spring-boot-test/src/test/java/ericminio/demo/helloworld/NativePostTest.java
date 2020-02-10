package ericminio.demo.helloworld;

import ericminio.support.HttpResponse;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

import static ericminio.support.PostRequest.post;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= RANDOM_PORT)
public class NativePostTest {

    @Autowired
    CsrfTokenRepository csrfTokenRepository;

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
        HttpResponse response = post(greetings, headers(), message.getBytes());

        assertThat( response.getStatusCode(), equalTo( 200 ) );
        assertThat( response.getBody(), equalTo( "My name is not Hal" ) );
    }

    private Map<String, String> headers() {
        Map<String, String> values = new HashMap<>();

        values.put("Authorization", "Basic " + Base64.encodeBase64String("user:correct-password".getBytes()));

        CsrfToken csrfToken = csrfTokenRepository.generateToken(null);
        values.put(csrfToken.getHeaderName(), csrfToken.getToken());
        values.put("Cookie", "XSRF-TOKEN=" + csrfToken.getToken());

        return values;
    }
}
