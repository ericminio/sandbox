package ericminio.http;

import com.sun.net.httpserver.HttpServer;
import ericminio.support.Stringify;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.net.InetSocketAddress;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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
        UploadPayload uploadPayload = new UploadPayload();
        uploadPayload.add(new FileInfo("one", "one.txt", "content #1"));
        HttpResponse response = upload("http://localhost:8001/zip", uploadPayload);
        assertThat(response.getStatusCode(), equalTo(200));

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(response.getBinaryBody());
        ZipInputStream zipInputStream = new ZipInputStream(byteArrayInputStream);
        ZipEntry entry = zipInputStream.getNextEntry();
        assertThat(entry.getName(), equalTo("one.txt"));

        String actual = new Stringify().inputStream(zipInputStream);
        assertThat(actual, equalTo("content #1"));
    }

    @Test
    public void worksWithTwoFiles() throws Exception {
        UploadPayload uploadPayload = new UploadPayload();
        uploadPayload.add(new FileInfo("one", "one.txt", "content #1"));
        uploadPayload.add(new FileInfo("two", "two.txt", "content #2"));
        HttpResponse response = upload("http://localhost:8001/zip", uploadPayload);
        assertThat(response.getStatusCode(), equalTo(200));

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(response.getBinaryBody());
        ZipInputStream zipInputStream = new ZipInputStream(byteArrayInputStream);
        ZipEntry entry = zipInputStream.getNextEntry();
        assertThat(entry.getName(), equalTo("one.txt"));

        String actual = new Stringify().inputStream(zipInputStream);
        assertThat(actual, equalTo("content #1"));

        entry = zipInputStream.getNextEntry();
        assertThat(entry.getName(), equalTo("two.txt"));

        actual = new Stringify().inputStream(zipInputStream);
        assertThat(actual, equalTo("content #2"));
    }
}
