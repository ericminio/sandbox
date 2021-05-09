package ericminio.http;

import com.sun.net.httpserver.HttpServer;
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
        FileSet set = new FileSet();
        set.add(new FileInfo("one", "one.txt", "content #1"));
        set.add(new FileInfo("two", "two.txt", "content #2"));
        set.add(new FormData("three", "content #3"));
        HttpResponse response = postForm("http://localhost:8004", set);

        assertThat(response.getStatusCode(), equalTo(200));
        assertThat(response.getContentType(), equalTo("text/plain"));
        assertThat(response.getBody(), equalTo("one two three "));
    }
}
