package sandbox;

import org.glassfish.grizzly.filterchain.*;
import org.glassfish.grizzly.http.*;
import org.glassfish.grizzly.memory.Buffers;
import org.glassfish.grizzly.nio.transport.TCPNIOTransport;
import org.glassfish.grizzly.nio.transport.TCPNIOTransportBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.glassfish.grizzly.http.Protocol.HTTP_1_1;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

public class StaticContentTest {

    private TCPNIOTransport transport;

    @Before
    public void startServer() throws Exception {
        FilterChainBuilder serverFilterChainBuilder = FilterChainBuilder.stateless();
        serverFilterChainBuilder.add(new TransportFilter());
        serverFilterChainBuilder.add(new HttpServerFilter());
        serverFilterChainBuilder.add(new BaseFilter() {
            @Override
            public NextAction handleRead(FilterChainContext ctx) {

                HttpResponsePacket response = HttpResponsePacket.builder(
                        (HttpRequestPacket) ctx.<HttpContent>getMessage().getHttpHeader())
                        .protocol(HTTP_1_1).status(200).build();
                ctx.write(response.httpContentBuilder().content(
                        Buffers.wrap(null, "hello Grizzly :)")).build());
                return ctx.getStopAction();
            }
        });
        transport = TCPNIOTransportBuilder.newInstance().build();
        transport.setProcessor(serverFilterChainBuilder.build());
        transport.bind("localhost", 8888);
        transport.start();
    }


    @After
    public void stopServer() throws Exception {
        transport.shutdownNow();    }

    @Test
    public void grizzlyCanServeStaticContent() throws Exception {

        assertThat(get("http://localhost:8888"), containsString("hello Grizzly :)"));
    }

    private String get(String uri) throws Exception {
        URL url = new URL( uri );
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        InputStream inputStream = connection.getInputStream();
        byte[] response = new byte[ inputStream.available() ];
        inputStream.read(response);

        return new String(response);
    }
}
