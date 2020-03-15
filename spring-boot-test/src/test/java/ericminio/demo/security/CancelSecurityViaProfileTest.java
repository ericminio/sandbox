package ericminio.demo.security;

import ericminio.support.BasicHeaders;
import ericminio.support.HttpResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.HttpURLConnection;
import java.net.URL;

import static ericminio.support.GetRequest.get;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment= RANDOM_PORT,
        properties = {
                "spring.profiles.active=unsecure"
        }
)
public class CancelSecurityViaProfileTest {

    @LocalServerPort
    int port;

    private String greeting;

    @Before
    public void buildEndpoint() {
        greeting = "http://localhost:"+ port +"/greeting";
    }

    @Test
    public void doesNotNeedToAuthenticate() throws Exception {
        HttpResponse response = get(greeting);

        assertThat( response.getStatusCode(), equalTo( 200 ) );
    }
}
