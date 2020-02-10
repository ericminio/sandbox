package ericminio.demo.primefactors.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import ericminio.demo.primefactors.domain.Decomposition;
import ericminio.support.HttpResponse;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

import static ericminio.support.GetRequest.get;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= RANDOM_PORT)
public class PrimeFactorsTest {

    @LocalServerPort
    int port;

    private Map<String, String> headers() {
        Map<String, String> values = new HashMap<>();

        values.put("Authorization", "Basic " + Base64.encodeBase64String("user:correct-password".getBytes()));

        return values;
    }

    @Test
    public void isAvailable() throws Exception {
        HttpResponse response = get("http://localhost:" + port + "/primeFactorsOf?number=1492", headers());

        assertThat(response.getStatusCode(), equalTo(200));
    }

    @Test
    public void returnsDecomposition() throws Exception {
        HttpResponse response = get("http://localhost:" + port + "/primeFactorsOf?number=1492", headers());
        Decomposition decomposition = new ObjectMapper().readValue(response.getBody(), Decomposition.class);

        assertThat(decomposition.getFactors(), equalTo(asList(2, 2, 373)));
    }

    @Test
    public void hasLimits() throws Exception {
        HttpResponse response = get("http://localhost:" + port + "/primeFactorsOf?number=10001", headers());

        assertThat(response.getStatusCode(), equalTo(501));
        assertThat(response.getBody(), equalTo("number <= 10000 expected"));
    }
}
