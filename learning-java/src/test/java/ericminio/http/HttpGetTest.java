package ericminio.http;

import com.sun.net.httpserver.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static ericminio.http.GetRequest.get;

public class HttpGetTest {

    private HttpServer server;

    @Before
    public void startServer() throws Exception {
        server = HttpServer.create( new InetSocketAddress( 8002 ), 0 );
        server.createContext( "/", exchange -> {
            exchange.sendResponseHeaders( 200, 0 );
            exchange.close();
        } );
        server.createContext( "/json", exchange -> {
            exchange.getResponseHeaders().add( "content-type", "application/json" );
            exchange.sendResponseHeaders( 200, 0 );
            exchange.close();
        } );
        server.createContext( "/need", exchange -> {
            String body = "Love";
            exchange.sendResponseHeaders( 200, body.length() );
            exchange.getResponseBody().write( body.getBytes() );
            exchange.close();
        } );
        server.createContext( "/greetings", exchange -> {
            String name = exchange.getRequestURI().getQuery();
            String body = "Received: " + name;
            exchange.sendResponseHeaders( 200, body.length() );
            exchange.getResponseBody().write( body.getBytes() );
            exchange.close();
        } );
        server.createContext( "/headers", exchange -> {
            String message = exchange.getRequestHeaders().getFirst("X-message");
            String body = "Received: " + message;
            exchange.sendResponseHeaders( 200, body.length() );
            exchange.getResponseBody().write( body.getBytes() );
            exchange.close();
        } );
        server.start();
    }

    @After
    public void stopServer() {
        server.stop( 1 );
    }

    @Test
    public void canAnswerOKForAnyGetRequest() throws Exception {
        URL url = new URL( "http://localhost:8002" );
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        assertThat( connection.getResponseCode(), equalTo( 200 ) );
    }

    @Test
    public void answersWithoutContentTypeByDefault() throws Exception {
        URL url = new URL( "http://localhost:8002" );
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        assertThat( connection.getHeaderField( "content-type" ), equalTo( null ) );
    }

    @Test
    public void canAnswerWithJsonContentType() throws Exception {
        URL url = new URL( "http://localhost:8002/json" );
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        assertThat( connection.getHeaderField( "content-type" ), equalTo( "application/json" ) );
    }

    @Test
    public void canSendContent() throws Exception {
        HttpResponse response = get( "http://localhost:8002/need" );

        assertThat( response.getBody(), equalTo( "Love" ) );
    }

    @Test
    public void canSpecifyQuery() throws Exception {
        HttpResponse response = get( "http://localhost:8002/greetings?name=Joe" );

        assertThat( response.getBody(), equalTo( "Received: name=Joe" ) );
    }

    @Test
    public void canSendHeaders() throws Exception {
        Map<String, String> headers = new HashMap<>();
        headers.put("X-message", "hello-world");
        HttpResponse response = get( "http://localhost:8002/headers", headers );

        assertThat( response.getBody(), equalTo( "Received: hello-world" ) );
    }
}
