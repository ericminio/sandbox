package ericminio.testing;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import support.HttpResponse;
import support.PutRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static support.GetRequest.get;
import static support.PostRequest.post;
import static support.PutRequest.put;

public class ServerStubTest {

    StubServer server;
    private int port = 5018;
    private Map<String, Object> variables;

    @Before
    public void startServer() throws IOException {
        variables = new HashMap<>();
        server = new StubServer("stub.json", variables);
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
        assertThat( response.getBody(), equalTo( "{\"old\":true,\"obsolete\":\"no\"}" ) );
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

    @Test
    public void canMatchIncomingBody() throws Exception {
        HttpResponse response = post( "http://localhost:"+port+"/object", "key" );

        assertThat( response.getStatusCode(), equalTo( 201 ) );
        assertThat( response.getContentType(), equalTo( "text/plain" ) );
        assertThat( response.getBody(), equalTo( "created key" ) );
    }

    @Test
    public void offersVariableSubstitutionInAnsweredBody() throws Exception {
        variables.put("who", "world");

        HttpResponse response = get( "http://localhost:"+port+"/greetings" );

        assertThat( response.getStatusCode(), equalTo( 200 ) );
        assertThat( response.getContentType(), equalTo( "text/plain" ) );
        assertThat( response.getBody(), equalTo( "hello world" ) );
    }

    @Test
    public void offersUrlSegmentExtraction() throws Exception {
        HttpResponse response = get( "http://localhost:"+port+"/echo/this-segment/and/that-other-segment" );

        assertThat( response.getStatusCode(), equalTo( 200 ) );
        assertThat( response.getContentType(), equalTo( "application/json" ) );
        assertThat( response.getBody(), equalTo( "{\"one\":\"this-segment\",\"two\":\"that-other-segment\"}" ) );
    }
}
