package ericminio.demo.primefactors;

import ericminio.domain.primefactors.Decomposition;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.NOT_IMPLEMENTED;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= RANDOM_PORT)
public class PrimeFactorsTest {

    @LocalServerPort
    int port;

    private String primeFactorsOf;

    @Before
    public void buildEndpoint() {
        primeFactorsOf = "http://localhost:"+ port +"/primeFactorsOf?number=";
    }

    @Test
    public void canDecompose2() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Decomposition> response = restTemplate.getForEntity(primeFactorsOf + "2", Decomposition.class);
        Decomposition decomposition = response.getBody();

        assertThat(decomposition.getFactors(), equalTo(Arrays.asList(2)));
    }

    @Test
    public void canDecompose1492() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Decomposition> response = restTemplate.getForEntity(primeFactorsOf + "1492", Decomposition.class);
        Decomposition decomposition = response.getBody();

        assertThat(decomposition.getFactors(), equalTo(Arrays.asList(2, 2, 373)));
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
        ResponseEntity<String> response = restTemplate.exchange(primeFactorsOf + "10001", GET, null, String.class);

        assertThat(response.getStatusCode(), equalTo(NOT_IMPLEMENTED));
        assertThat(response.getBody(), equalTo("number <= 10000 expected"));
    }
}
