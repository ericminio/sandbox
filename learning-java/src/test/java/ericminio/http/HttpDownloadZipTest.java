package ericminio.http;

import com.sun.net.httpserver.HttpServer;
import ericminio.zip.FileSet;
import ericminio.zip.Unzip;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.InetSocketAddress;

import static ericminio.http.PostFormRequest.postForm;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class HttpDownloadZipTest {

    private HttpServer server;

    @Before
    public void startServer() throws Exception {
        server = HttpServer.create( new InetSocketAddress( 8001 ), 0 );
        server.createContext( "/zip", new EchoZipPayload());
        server.start();
    }

    @After
    public void stopServer() {
        server.stop( 0 );
    }

    @Test
    public void worksForOneFile() throws Exception {
        FormDataSet formDataSet = new FormDataSet();
        formDataSet.add(new FileFormData("one", "one.txt", "content #1"));
        HttpResponse response = postForm("http://localhost:8001/zip", formDataSet);
        assertThat(response.getStatusCode(), equalTo(200));
        assertThat(response.getContentType(), equalTo("application/zip"));
        assertThat(response.getContentDisposition(), equalTo("attachment; filename=\"download.zip\""));

        FileSet output = new Unzip().please(response.getBinaryBody());
        assertThat(output.size(), equalTo(1));
        assertThat(output.get(0).getFileName(), equalTo("one.txt"));
        assertThat(output.get(0).getContent(), equalTo("content #1"));
    }

    @Test
    public void worksWithTwoFiles() throws Exception {
        FormDataSet formDataSet = new FormDataSet();
        formDataSet.add(new FileFormData("one", "one.txt", "content #1"));
        formDataSet.add(new FileFormData("two", "two.txt", "content #2"));
        HttpResponse response = postForm("http://localhost:8001/zip", formDataSet);
        assertThat(response.getStatusCode(), equalTo(200));

        FileSet output = new Unzip().please(response.getBinaryBody());
        assertThat(output.size(), equalTo(2));
        assertThat(output.get(0).getFileName(), equalTo("one.txt"));
        assertThat(output.get(0).getContent(), equalTo("content #1"));
        assertThat(output.get(1).getFileName(), equalTo("two.txt"));
        assertThat(output.get(1).getContent(), equalTo("content #2"));
    }
}
