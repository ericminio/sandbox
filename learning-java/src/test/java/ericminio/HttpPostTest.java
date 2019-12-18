package ericminio;

import com.sun.net.httpserver.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

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
        URL url = new URL( "http://localhost:8001/create" );
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        byte[] postData = "key".getBytes( StandardCharsets.UTF_8 );
        connection.setRequestProperty( "Content-Length", Integer.toString(postData.length));
        DataOutputStream writer = new DataOutputStream( connection.getOutputStream());
        writer.write(postData);

        assertThat( connection.getResponseCode(), equalTo( 200 ) );

        InputStream inputStream = connection.getInputStream();
        byte[] response = new byte[ inputStream.available() ];
        inputStream.read( response );

        assertThat( new String( response ), equalTo( "method=POST data=key" ) );
    }
}
