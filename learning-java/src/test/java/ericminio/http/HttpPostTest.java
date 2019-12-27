package ericminio.http;

import com.sun.net.httpserver.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import support.HttpResponse;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.util.stream.Collectors;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static support.PostRequest.post;

public class HttpPostTest {

    private HttpServer server;

    @Before
    public void startServer() throws Exception {
        server = HttpServer.create( new InetSocketAddress( 8001 ), 0 );
        server.createContext( "/create", exchange -> {
            String method = exchange.getRequestMethod();
            BufferedReader data = new BufferedReader(new InputStreamReader(exchange.getRequestBody()));
            String body = "method=" + method + " data=" + data.lines().collect(Collectors.joining());
            exchange.sendResponseHeaders( 200, body.length() );
            exchange.getResponseBody().write( body.getBytes() );
            exchange.close();
        } );
        server.start();
    }

    @After
    public void stopServer() {
        server.stop( 0 );
    }

    @Test
    public void canSendDataViaPost() throws Exception {
        HttpResponse response = post("http://localhost:8001/create", "key" );

        assertThat( response.getStatusCode(), equalTo( 200 ) );
        assertThat( response.getBody(), equalTo( "method=POST data=key" ) );
    }
}
