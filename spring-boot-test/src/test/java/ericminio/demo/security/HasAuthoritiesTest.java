package ericminio.demo.security;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= RANDOM_PORT)
public class HasAuthoritiesTest {

    @LocalServerPort
    int port;

    private String needsAuthority;

    @Before
    public void buildEndpoint() {
        needsAuthority = "http://localhost:"+ port +"/needs-authority";
    }

    @Test
    public void needsValidCredentials() {
        TestRestTemplate restTemplate = new TestRestTemplate("user", "correct-password");
        ResponseEntity<String> response = restTemplate.getForEntity(needsAuthority, String.class);

        assertThat( response.getStatusCode(), equalTo( HttpStatus.FORBIDDEN ) );
    }

    @Test
    public void canAuthenticateWithCorrectAuthorities() {
        TestRestTemplate restTemplate = new TestRestTemplate("user-with-valid-authority", "correct-password");
        ResponseEntity<String> response = restTemplate.getForEntity(needsAuthority, String.class);

        assertThat( response.getStatusCode(), equalTo( HttpStatus.OK ) );
        assertThat( response.getBody(), equalTo( "Hello ADMIN" ) );
    }
}
