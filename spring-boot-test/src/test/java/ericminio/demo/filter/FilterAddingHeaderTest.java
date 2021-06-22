package ericminio.demo.filter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= RANDOM_PORT)
public class FilterAddingHeaderTest {

    @LocalServerPort
    int port;

    @Autowired
    RestTemplate restTemplate;

    private String filtered;

    @Before
    public void buildEndpoint() {
        filtered = "http://localhost:"+ port +"/filtered";
    }

    @Test
    public void endPointIsAvailable() {
        ResponseEntity<Filtered> response = restTemplate.getForEntity(filtered, Filtered.class);

        assertThat( response.getStatusCode(), equalTo( HttpStatus.OK ) );
    }

    @Test
    public void filterCanAddHeader() {
        ResponseEntity<Filtered> response = restTemplate.getForEntity(filtered, Filtered.class);
        Filtered filtered = response.getBody();

        assertThat( filtered.getValue(), equalTo( "filtered" ) );
    }
}
