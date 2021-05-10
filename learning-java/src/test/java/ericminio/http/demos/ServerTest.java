package ericminio.http.demos;

import ericminio.http.FileFormData;
import ericminio.http.FormData;
import ericminio.http.FormDataSet;
import ericminio.http.HttpResponse;
import ericminio.zip.FileSet;
import ericminio.zip.Unzip;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static ericminio.http.GetRequest.get;
import static ericminio.http.PostFormRequest.postForm;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class ServerTest {

    Server server;
    Integer port = 8005;

    @Before
    public void start() throws IOException {
        server = new Server(port);
        server.start();
    }
    @After
    public void stop() {
        server.stop();
    }

    @Test
    public void servesIndex() throws Exception {
        HttpResponse response = get( "http://localhost:" + port);

        assertThat(response.getStatusCode(), equalTo( 200));
        assertThat(response.getContentType(), equalTo("text/html"));
        assertThat(response.getBody(), containsString( "<!DOCTYPE html>"));
    }

    @Test
    public void acceptsExpectedForm() throws Exception {
        FormDataSet formDataSet = new FormDataSet();
        formDataSet.add(new FileFormData("one", "one.txt", "content #1"));
        formDataSet.add(new FileFormData("two", "two.txt", "content #2"));
        formDataSet.add(new FormData("three", "content #3"));
        HttpResponse response = postForm("http://localhost:" + port + "/explore", formDataSet);

        assertThat(response.getStatusCode(), equalTo(200));
        assertThat(response.getContentType(), equalTo("application/zip"));

        FileSet output = new Unzip().please(response.getBinaryBody());
        assertThat(output.size(), equalTo(2));
    }
}
