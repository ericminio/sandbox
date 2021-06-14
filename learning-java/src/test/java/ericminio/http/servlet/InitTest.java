package ericminio.http.servlet;

import com.sun.net.httpserver.HttpServer;
import ericminio.http.HttpResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import support.FakeServletConfig;

import java.net.InetSocketAddress;

import static ericminio.http.GetRequest.get;
import static java.lang.String.format;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class InitTest {

    private HttpServer server;
    private int port = 8003;

    @Before
    public void startServer() throws Exception {
        server = HttpServer.create( new InetSocketAddress( port ), 0 );
        new AddContextFrom(new Init()).in(server, new FakeServletConfig("call count = "));
        server.start();
    }

    @After
    public void stopServer() {
        server.stop( 1 );
    }

    @Test
    public void callsInit() throws Exception {
        HttpResponse response = get( format("http://localhost:%d/init", port));

        assertThat(response.getBody(), equalTo("call count = 1"));
    }

    @Test
    public void callsInitOnlyOnce() throws Exception {
        get( format("http://localhost:%d/init", port));
        HttpResponse response = get( format("http://localhost:%d/init", port));

        assertThat(response.getBody(), equalTo("call count = 1"));
    }

}
