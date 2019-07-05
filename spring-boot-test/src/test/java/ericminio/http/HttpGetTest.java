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
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

@RunWith(SpringRunner.class)
@SpringBootApplication
@SpringBootTest(webEnvironment= DEFINED_PORT)
public class HttpGetTest {

    private String endpoint = "http://localhost:8080/greeting";

    @Test
    public void isAvailable() throws Exception {
        URL url = new URL(endpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        assertThat( connection.getResponseCode(), equalTo( 200 ) );
    }

    @Test
    public void answersWithJson() throws Exception {
        HttpResponse response = get(endpoint);

        assertThat( response.getContentType(), equalTo( "application/json;charset=UTF-8" ) );
    }

    @Test
    public void sendsGreetings() throws Exception {
        HttpResponse response = get( endpoint + "?name=Joe" );

        assertThat( response.getBody(), equalTo( "{\"content\":\"Hello, Joe!\"}" ) );
    }

    @Test
    public void defaultGreetings() throws Exception {
        HttpResponse response = get(endpoint);

        assertThat( response.getBody(), equalTo( "{\"content\":\"Hello, World!\"}" ) );
    }
}
