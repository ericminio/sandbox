package ericminio.jaxrs;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import support.HttpResponse;
import javax.ws.rs.ext.RuntimeDelegate;
import java.net.InetSocketAddress;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static support.GetRequest.get;

public class JaxRsTest {

    private HttpServer server;

    @Before
    public void startServer() throws Exception {
        server = HttpServer.create( new InetSocketAddress( 8003 ), 0 );
        HttpHandler handler = RuntimeDelegate.getInstance().createEndpoint(new GreetingsApplication(), HttpHandler.class);
        server.createContext("/", handler);
        server.start();
    }
    @After
    public void stopServer() {
        server.stop( 1 );
    }

    @Test
    public void exposesEndpoint() throws Exception {
        HttpResponse response = get( "http://localhost:8003/api/greetings" );

        assertThat( response.getStatusCode(), equalTo( 200 ) );
    }
}
