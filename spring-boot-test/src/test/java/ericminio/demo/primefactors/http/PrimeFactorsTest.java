package ericminio.demo.primefactors.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import ericminio.demo.primefactors.domain.Decomposition;
import ericminio.support.HttpResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import static ericminio.support.GetRequest.get;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.NOT_IMPLEMENTED;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= RANDOM_PORT)
public class PrimeFactorsTest {

    @LocalServerPort
    int port;

    @Test
    public void isAvailable() throws Exception {
        HttpResponse response = get("http://localhost:" + port + "/primeFactorsOf?number=1492");

        assertThat(response.getStatusCode(), equalTo(200));
    }

    @Test
    public void returnsDecomposition() throws Exception {
        HttpResponse response = get("http://localhost:" + port + "/primeFactorsOf?number=1492");
        Decomposition decomposition = new ObjectMapper().readValue(response.getBody(), Decomposition.class);

        assertThat(decomposition.getFactors(), equalTo(asList(2, 2, 373)));
    }

    @Test
    public void hasLimits() throws Exception {
        HttpResponse response = get("http://localhost:" + port + "/primeFactorsOf?number=10001");

        assertThat(response.getStatusCode(), equalTo(501));
        assertThat(response.getBody(), equalTo("number <= 10000 expected"));
    }

    @Autowired
    RestTemplateBuilder restTemplateBuilder;

    @Test
    public void isLimited() {
        RestTemplate restTemplate = restTemplateBuilder.errorHandler(new ResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse clientHttpResponse) {
                return false;
            }
            @Override
            public void handleError(ClientHttpResponse clientHttpResponse) {
            }
        }).build();
        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/primeFactorsOf?number=10001", GET, null, String.class);

        assertThat(response.getStatusCode(), equalTo(NOT_IMPLEMENTED));
        assertThat(response.getBody(), equalTo("number <= 10000 expected"));
    }

}
