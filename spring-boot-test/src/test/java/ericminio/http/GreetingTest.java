package ericminio.http;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

@RunWith(SpringRunner.class)
@SpringBootApplication
@SpringBootTest(webEnvironment= DEFINED_PORT)
public class GreetingTest {

    @Test
    public void isAvailable() throws Exception {
        URL url = new URL( "http://localhost:8080/greeting" );
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        assertThat( connection.getResponseCode(), equalTo( 200 ) );
    }

    
    public void answersWithJson() throws Exception {
        URL url = new URL( "http://localhost:8000/greeting" );
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        assertThat( connection.getHeaderField( "content-type" ), equalTo( "application/json" ) );
    }

    @Test
    public void sendsGreetings() throws Exception {
        URL url = new URL( "http://localhost:8080/greeting?name=Joe" );
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        InputStream inputStream = connection.getInputStream();
        byte[] response = new byte[ inputStream.available() ];
        inputStream.read( response );

        assertThat( new String( response ), equalTo( "{\"content\":\"Hello, Joe!\"}" ) );
    }
}
