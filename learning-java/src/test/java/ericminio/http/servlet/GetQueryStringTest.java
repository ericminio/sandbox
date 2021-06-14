package ericminio.http.servlet;

import com.sun.net.httpserver.HttpServer;
import ericminio.http.HttpResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.InetSocketAddress;

import static ericminio.http.GetRequest.get;
import static java.lang.String.format;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class GetQueryStringTest {

    private HttpServer server;
    private int port = 8003;

    @Before
    public void startServer() throws Exception {
        server = HttpServer.create( new InetSocketAddress( port ), 0 );
        new AddContextFrom(new GetQueryString()).in(server);
        server.start();
    }

    @After
    public void stopServer() {
        server.stop( 1 );
    }

    @Test
    public void isAvailable() throws Exception {
        HttpResponse response = get( format("http://localhost:%d/query?hello=world", port));

        assertThat(response.getBody(), equalTo("received: hello=world"));
    }
}
