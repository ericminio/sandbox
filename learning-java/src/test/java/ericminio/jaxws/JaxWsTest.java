package ericminio.jaxws;

import ericminio.http.HttpResponse;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.xml.ws.Endpoint;
import java.util.HashMap;

import static ericminio.http.GetRequest.get;
import static ericminio.http.PostRequest.post;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class JaxWsTest {

    @BeforeClass
    public static void publishService() {
        Endpoint.publish("http://localhost:8005/productservice", new ProductService());
    }

    @Test
    public void exposesWsdl() throws Exception {
        HttpResponse response = get( "http://localhost:8005/productservice?wsdl" );

        assertThat( response.getStatusCode(), equalTo( 200 ) );
        assertThat( response.getBody(), startsWith( "<?xml" ) );
    }

    @Test
    public void exposesService() throws Exception {
        String body = "" +
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
                "    xmlns:tns=\"http://jaxws.ericminio/\">\n" +
                "    <soapenv:Header/>\n" +
                "    <soapenv:Body>\n" +
                "        <tns:find />\n" +
                "    </soapenv:Body>\n" +
                "</soapenv:Envelope>";
        HttpResponse response = post("http://localhost:8005/productservice", new HashMap<>(), body.getBytes(), "text/xml");

        assertThat( response.getStatusCode(), equalTo( 200 ) );
        assertThat( response.getBody(), containsString( "<name>this-name</name>" ) );
    }
}
