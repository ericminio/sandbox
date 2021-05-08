package ericminio.http;

import com.sun.net.httpserver.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.InetSocketAddress;

import static ericminio.http.UploadRequest.upload;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class HttpUploadTest {

    private HttpServer server;

    @Before
    public void startServer() throws Exception {
        server = HttpServer.create( new InetSocketAddress( 8001 ), 0 );
        server.createContext( "/token", new EchoUploadToken());
        server.createContext( "/filename", new EchoUploadedFilename());
        server.start();
    }

    @After
    public void stopServer() {
        server.stop( 0 );
    }

    @Test
    public void echoToken() throws Exception {
        HttpResponse response = upload("http://localhost:8001/token", "any content" );

        assertThat( response.getStatusCode(), equalTo( 200 ) );
        assertThat( response.getBody(), equalTo( "-----token" ) );
    }

    @Test
    public void echoFilename() throws Exception {
        HttpResponse response = upload("http://localhost:8001/filename", "any content" );

        assertThat( response.getStatusCode(), equalTo( 200 ) );
        assertThat( response.getBody(), equalTo( "any.txt" ) );
    }
}
