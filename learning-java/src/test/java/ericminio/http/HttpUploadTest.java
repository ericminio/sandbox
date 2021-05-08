package ericminio.http;

import com.sun.net.httpserver.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.InetSocketAddress;

import static ericminio.http.UploadProtocol.boundary;
import static ericminio.http.UploadProtocol.hyphens;
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
        server.createContext( "/filenames", new EchoUploadedFilenames());
        server.start();
    }

    @After
    public void stopServer() {
        server.stop( 0 );
    }

    @Test
    public void echoToken() throws Exception {
        UploadPayload uploadPayload = new UploadPayload();
        uploadPayload.add(new FileInfo("any", "any.txt", "any content"));
        HttpResponse response = upload("http://localhost:8001/token", uploadPayload);

        assertThat( response.getStatusCode(), equalTo( 200 ) );
        assertThat( response.getBody(), equalTo( hyphens + boundary ) );
    }

    @Test
    public void echoFilename() throws Exception {
        UploadPayload uploadPayload = new UploadPayload();
        uploadPayload.add(new FileInfo("any", "any.txt", "any content"));
        HttpResponse response = upload("http://localhost:8001/filename", uploadPayload);

        assertThat( response.getStatusCode(), equalTo( 200 ) );
        assertThat( response.getBody(), equalTo( "any.txt" ) );
    }

    @Test
    public void echoFilenames() throws Exception {
        UploadPayload uploadPayload = new UploadPayload();
        uploadPayload.add(new FileInfo("any", "one.txt", "any content"));
        uploadPayload.add(new FileInfo("any", "two.txt", "any content"));
        HttpResponse response = upload("http://localhost:8001/filenames", uploadPayload);

        assertThat( response.getStatusCode(), equalTo( 200 ) );
        assertThat( response.getBody(), equalTo( "one.txt two.txt " ) );
    }
}
