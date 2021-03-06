package ericminio.http;

import com.sun.net.httpserver.HttpServer;
import ericminio.zip.FileSet;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.InetSocketAddress;

import static ericminio.http.FormDataProtocol.boundary;
import static ericminio.http.FormDataProtocol.hyphens;
import static ericminio.http.PostFormRequest.postForm;
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
        FormDataSet formDataSet = new FormDataSet();
        formDataSet.add(new FileFormData("any", "any.txt", "any content"));
        HttpResponse response = postForm("http://localhost:8001/token", formDataSet);

        assertThat( response.getStatusCode(), equalTo( 200 ) );
        assertThat( response.getBody(), equalTo( hyphens + boundary ) );
    }

    @Test
    public void echoFilename() throws Exception {
        FormDataSet formDataSet = new FormDataSet();
        formDataSet.add(new FileFormData("any", "any.txt", "any content"));
        HttpResponse response = postForm("http://localhost:8001/filename", formDataSet);

        assertThat( response.getStatusCode(), equalTo( 200 ) );
        assertThat( response.getBody(), equalTo( "any.txt" ) );
    }

    @Test
    public void echoFilenames() throws Exception {
        FormDataSet formDataSet = new FormDataSet();
        formDataSet.add(new FileFormData("any", "one.txt", "any content"));
        formDataSet.add(new FileFormData("any", "two.txt", "any content"));
        HttpResponse response = postForm("http://localhost:8001/filenames", formDataSet);

        assertThat( response.getStatusCode(), equalTo( 200 ) );
        assertThat( response.getBody(), equalTo( "one.txt two.txt " ) );
    }
}
