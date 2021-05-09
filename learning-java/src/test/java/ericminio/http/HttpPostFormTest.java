package ericminio.http;

import com.sun.net.httpserver.HttpServer;
import ericminio.zip.FileSet;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.InetSocketAddress;

import static ericminio.http.PostFormRequest.postForm;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class HttpPostFormTest {

    private HttpServer server;

    @Before
    public void startServer() throws Exception {
        server = HttpServer.create( new InetSocketAddress( 8004 ), 0 );
        server.createContext( "/", new EchoFormNames());
        server.start();
    }

    @After
    public void stopServer() {
        server.stop( 0 );
    }

    @Test
    public void works() throws Exception {
        FormDataSet formDataSet = new FormDataSet();
        formDataSet.add(new FileFormData("one", "one.txt", "content #1"));
        formDataSet.add(new FileFormData("two", "two.txt", "content #2"));
        formDataSet.add(new FormData("three", "content #3"));
        HttpResponse response = postForm("http://localhost:8004", formDataSet);

        assertThat(response.getStatusCode(), equalTo(200));
        assertThat(response.getContentType(), equalTo("text/plain"));
        assertThat(response.getBody(), equalTo("one two three "));
    }
}
