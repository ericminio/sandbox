package ericminio.http;

import com.sun.net.httpserver.HttpServer;
import ericminio.zip.Unzip;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.InetSocketAddress;

import static ericminio.http.UploadRequest.upload;
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
        FileSet fileSet = new FileSet();
        fileSet.add(new FileInfo("one", "one.txt", "content #1"));
        HttpResponse response = upload("http://localhost:8001/zip", fileSet);
        assertThat(response.getStatusCode(), equalTo(200));
        assertThat(response.getContentType(), equalTo("application/zip"));
        assertThat(response.getContentDisposition(), equalTo("attachment; filename=\"download.zip\""));

        FileSet output = new Unzip().please(response.getBinaryBody());
        assertThat(output.size(), equalTo(1));
        assertThat(output.getFileInfo(0).getFileName(), equalTo("one.txt"));
        assertThat(output.getFileInfo(0).getContent(), equalTo("content #1"));
    }

    @Test
    public void worksWithTwoFiles() throws Exception {
        FileSet input = new FileSet();
        input.add(new FileInfo("one", "one.txt", "content #1"));
        input.add(new FileInfo("two", "two.txt", "content #2"));
        HttpResponse response = upload("http://localhost:8001/zip", input);
        assertThat(response.getStatusCode(), equalTo(200));

        FileSet output = new Unzip().please(response.getBinaryBody());
        assertThat(output.size(), equalTo(2));
        assertThat(output.getFileInfo(0).getFileName(), equalTo("one.txt"));
        assertThat(output.getFileInfo(0).getContent(), equalTo("content #1"));
        assertThat(output.getFileInfo(1).getFileName(), equalTo("two.txt"));
        assertThat(output.getFileInfo(1).getContent(), equalTo("content #2"));
    }
}
