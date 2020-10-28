package ericminio.demo.primefactors.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import ericminio.demo.primefactors.domain.Decomposition;
import ericminio.support.BasicHeaders;
import ericminio.support.HttpResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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

    @Autowired
    BasicHeaders basic;

    @Test
    public void isAvailable() throws Exception {
        HttpResponse response = get("http://localhost:" + port + "/primeFactorsOf?number=300", basic.headers());

        assertThat(response.getStatusCode(), equalTo(200));
    }

    @Test
    public void returnsDecomposition() throws Exception {
        HttpResponse response = get("http://localhost:" + port + "/primeFactorsOf?number=1492", basic.headers());
        Decomposition decomposition = new ObjectMapper().readValue(response.getBody(), Decomposition.class);

        assertThat(decomposition.getFactors(), equalTo(asList(2, 2, 373)));
    }

    @Test
    public void hasLimits() throws Exception {
        HttpResponse response = get("http://localhost:" + port + "/primeFactorsOf?number=10001", basic.headers());

        assertThat(response.getStatusCode(), equalTo(501));
        assertThat(response.getBody(), equalTo("number <= 10000 expected"));
    }

    @Test
    public void isRateLimitedByInputNumber() throws Exception {
        StatusTracker statusTracker = new StatusTracker();
        updateStatus(statusTracker, get("http://localhost:" + port + "/primeFactorsOf?number=10", basic.headers()));
        assertThat(statusTracker.getStatusCode(), equalTo(200));
        updateStatus(statusTracker, get("http://localhost:" + port + "/primeFactorsOf?number=15", basic.headers()));
        assertThat(statusTracker.getStatusCode(), equalTo(200));
        updateStatus(statusTracker, get("http://localhost:" + port + "/primeFactorsOf?number=20", basic.headers()));
        assertThat(statusTracker.getStatusCode(), equalTo(200));
        updateStatus(statusTracker, get("http://localhost:" + port + "/primeFactorsOf?number=20", basic.headers()));
        assertThat(statusTracker.getStatusCode(), equalTo(429));

        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.schedule(() -> {
            try {
                updateStatus(statusTracker, get("http://localhost:" + port + "/primeFactorsOf?number=20", basic.headers()));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }, 2, TimeUnit.SECONDS);
        scheduledExecutorService.awaitTermination(3, TimeUnit.SECONDS);
        scheduledExecutorService.shutdown();

        assertThat(statusTracker.getStatusCode(), equalTo(200));
    }

    private void updateStatus(StatusTracker statusTracker, HttpResponse httpResponse) {
        statusTracker.setStatusCode(httpResponse.getStatusCode());
    }

    class StatusTracker {
        private int statusCode;

        public int getStatusCode() {
            return statusCode;
        }

        public void setStatusCode(int statusCode) {
            this.statusCode = statusCode;
        }
    }
}
