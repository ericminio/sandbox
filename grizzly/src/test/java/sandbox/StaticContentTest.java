package sandbox;

import http.Resource;
import org.glassfish.grizzly.Buffer;
import org.glassfish.grizzly.filterchain.*;
import org.glassfish.grizzly.http.*;
import org.glassfish.grizzly.memory.Buffers;
import org.glassfish.grizzly.nio.transport.TCPNIOTransport;
import org.glassfish.grizzly.nio.transport.TCPNIOTransportBuilder;
import org.glassfish.grizzly.utils.StringFilter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;

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
                HttpContent httpContent = ctx.getMessage();
                HttpRequestPacket request = (HttpRequestPacket) httpContent.getHttpHeader();
                HttpResponsePacket response = HttpResponsePacket.builder(request).protocol(request.getProtocol()).status(200).reasonPhrase("OK").build();
                HttpContent content = response.httpContentBuilder().content(Buffers.wrap(null, "hello Grizzly :)")).build();
                ctx.write(content);

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

        assertThat(Resource.withUrl("http://localhost:8888"), containsString("hello Grizzly :)"));
    }
}
