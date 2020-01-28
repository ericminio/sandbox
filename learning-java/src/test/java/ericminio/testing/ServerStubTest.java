package ericminio.testing;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import support.HttpResponse;

import java.io.IOException;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static support.GetRequest.get;
import static support.PostRequest.post;
import static support.PutRequest.put;

public class ServerStubTest {

    StubServer server;
    private int port = 5018;

    @Before
    public void startServer() throws IOException {
        server = new StubServer("stub.json");
        server.start(port);
    }
    @After
    public void stopServer() {
        server.stop();
    }

    @Test
    public void hasDefaultAnswer() throws Exception {
        HttpResponse response = get( "http://localhost:"+port+"/default" );

        assertThat( response.getStatusCode(), equalTo( 200 ) );
        assertThat( response.getContentType(), equalTo( "application/json" ) );
        assertThat( response.getBody(), equalTo( "{\"alive\":true}" ) );
    }

    @Test
    public void canMatchIncomingUrl() throws Exception {
        HttpResponse response = get( "http://localhost:"+port+"/ping" );

        assertThat( response.getStatusCode(), equalTo( 200 ) );
        assertThat( response.getContentType(), equalTo( "text/plain" ) );
        assertThat( response.getBody(), equalTo( "pong" ) );
    }

    @Test
    public void canMatchIncomingMethod() throws Exception {
        HttpResponse response = post( "http://localhost:"+port+"/object", "anything" );

        assertThat( response.getStatusCode(), equalTo( 201 ) );
        assertThat( response.getContentType(), equalTo( "text/plain" ) );
        assertThat( response.getBody(), equalTo( "created" ) );

        response = put( "http://localhost:"+port+"/object", "anything" );

        assertThat( response.getStatusCode(), equalTo( 200 ) );
        assertThat( response.getContentType(), equalTo( "text/plain" ) );
        assertThat( response.getBody(), equalTo( "updated" ) );
    }
}
