package ericminio.testing;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import support.HttpResponse;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static support.GetRequest.get;
import static support.PostRequest.post;
import static support.PutRequest.put;

public class ServerStubTest {

    ServerStub server;
    private int port = 5018;

    @Before
    public void startServer() throws IOException {
        server = new ServerStub("stub.json");
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
        server.getVariables().put("who", "world");

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

    @Test
    public void offersFunctionCallInAnsweredBody() throws Exception {
        server.getFunctions().put("combine", (incoming, variables, parameters) ->
                variables.get("groupCount") + ": " + incoming.getBody() + " " + variables.get("group-1")
        );
        HttpResponse response = post( "http://localhost:"+port+"/modify/world", "hello" );

        assertThat( response.getStatusCode(), equalTo( 200 ) );
        assertThat( response.getContentType(), equalTo( "application/json" ) );
        assertThat( response.getBody(), equalTo( "{\"message\":\"1: hello world\"}" ) );
    }

    @Test
    public void functionCanGenerateObject() throws Exception {
        server.getFunctions().put("create-object", (incoming, variables, parameters) ->
                Arrays.asList("one", "two").stream().map(v ->new HashMap<String, Object>() {{ put("value", v); }}).collect(Collectors.toList())
        );
        HttpResponse response = get("http://localhost:"+port+"/function-collection");

        assertThat( response.getStatusCode(), equalTo( 200 ) );
        assertThat( response.getContentType(), equalTo( "application/json" ) );
        assertThat( response.getBody(), equalTo( "{\"values\":[{\"value\":\"one\"},{\"value\":\"two\"}]}" ) );
    }

    @Test
    public void doNotRunFunctionWhenYouDoNotNeed() throws Exception {
        server.getFunctions().put("any", (incoming, variables, parameters) -> {
            throw new RuntimeException("should not call me");
        });
        HttpResponse response = get("http://localhost:"+port+"/no-function");

        assertThat( response.getStatusCode(), equalTo( 200 ) );
        assertThat( response.getContentType(), equalTo( "application/json" ) );
        assertThat( response.getBody(), equalTo( "{\"all\":\"good\"}" ) );
    }

    @Test
    public void resistsExceptionInFunctionCalls() throws Exception {
        server.getFunctions().put("any", (incoming, variables, parameters) -> {
            throw new RuntimeException("resist me");
        });
        HttpResponse response = get("http://localhost:"+port+"/exception-in-function");

        assertThat( response.getStatusCode(), equalTo( 500 ) );
        assertThat( response.getContentType(), equalTo( "text/plain" ) );
        assertThat( response.getBody(), equalTo( "resist me" ) );
    }

    @Test
    public void generatedObjectCanBeString() throws Exception {
        server.getFunctions().put("plain-text", (incoming, variables, parameters) ->
                "hello world"
        );
        HttpResponse response = get("http://localhost:"+port+"/function-plain-text");

        assertThat( response.getStatusCode(), equalTo( 200 ) );
        assertThat( response.getContentType(), equalTo( "text/plain" ) );
        assertThat( response.getBody(), equalTo( "hello world :)" ) );
    }

    @Test
    public void generatedObjectCanBeObject() throws Exception {
        server.getFunctions().put("simple-object", (incoming, variables, parameters) ->
                new HashMap<String, Object>() {{ put("field", "value"); }}
        );
        HttpResponse response = get("http://localhost:"+port+"/simple-object");

        assertThat( response.getStatusCode(), equalTo( 200 ) );
        assertThat( response.getContentType(), equalTo( "text/plain" ) );
        assertThat( response.getBody(), equalTo( "{\"field\":\"value\"}" ) );
    }

    @Test
    public void answeredStatusCodeCanBeDynamic() throws Exception {
        server.getFunctions().put("dynamic-status-code", (incoming, variables, parameters) ->
                222
        );
        HttpResponse response = get("http://localhost:"+port+"/status-code");

        assertThat( response.getStatusCode(), equalTo( 222 ) );
    }
    @Test(expected = SocketTimeoutException.class)
    public void offersDelayedAnswerConfiguration() throws Exception {
        get( "http://localhost:"+port+"/delayed", 100 );
    }
    @Test
    public void delayedAnswerCanStillBeSuccessful() throws Exception {
        HttpResponse response = get("http://localhost:" + port + "/delayed", 200);

        assertThat( response.getStatusCode(), equalTo( 200 ) );
        assertThat( response.getContentType(), equalTo( "text/plain" ) );
        assertThat( response.getBody(), equalTo( "delayed" ) );
    }
}
