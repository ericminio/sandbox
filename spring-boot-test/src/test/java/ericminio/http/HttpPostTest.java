package ericminio.http;

import ericminio.support.HttpResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.HttpURLConnection;
import java.net.URL;

import static ericminio.support.GetRequest.get;
import static ericminio.support.PostRequest.post;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= DEFINED_PORT)
public class HttpPostTest {

    private String endpoint = "http://localhost:8080/greetings";

    @Test
    public void works() throws Exception {
        String message = "{\"content\":\"Hello, Hal!\"}";
        HttpResponse response = post(endpoint, message.getBytes());

        assertThat( response.getBody(), equalTo( "My name is not Hal" ) );
    }
}
