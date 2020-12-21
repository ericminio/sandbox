package ericminio.http;

import com.sun.net.httpserver.HttpServer;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ericminio.support.Stringify;

import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;
import java.net.URL;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class SleepingServerTest {

    private static HttpServer server;
    private static boolean SLEEPING;

    @Before
    public void reset() {
        SLEEPING = true;
    }

    @BeforeClass
    public static void startServer() throws Exception {
        server = HttpServer.create( new InetSocketAddress( 8003 ), 0 );
        server.createContext( "/need", exchange -> {
            if (SLEEPING) {
                SLEEPING = false;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            String body = "Love";
            exchange.sendResponseHeaders( 200, body.length() );
            exchange.getResponseBody().write( body.getBytes() );
            exchange.close();
        } );
        server.start();
    }

    @AfterClass
    public static void stopServer() {
        server.stop( 0 );
    }

    @Test
    public void canTimeout() throws Exception {
        URL url = new URL( "http://localhost:8003/need" );
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setReadTimeout(300);
        try {
            connection.getResponseCode();
            fail();
        } catch (SocketTimeoutException e) {
            assertThat(e.getMessage(), equalTo("Read timed out"));
        }
    }

    @Test
    public void requiresPatience() throws Exception {
        HttpResponse response = getPatiently( "http://localhost:8003/need" );

        assertThat( response.getStatusCode(), equalTo( 200 ) );
        assertThat( response.getBody(), equalTo( "Love" ) );
    }

    private HttpResponse getPatiently(String url) {
        int tryAgain = 7;
        do {
            try {
                HttpURLConnection request = (HttpURLConnection) new URL( url ).openConnection();
                request.setReadTimeout(300);
                HttpResponse response = new HttpResponse();
                response.setStatusCode(request.getResponseCode());
                response.setBody(new Stringify().inputStream(request.getInputStream()));
                return response;
            } catch (Exception e) {
                tryAgain -= 1;
            }
        }
        while (tryAgain > 0);

        return new HttpResponse() {{
           setStatusCode(503);
        }};
    }
}
