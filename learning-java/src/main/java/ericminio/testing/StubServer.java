package ericminio.testing;

import com.sun.net.httpserver.HttpServer;
import ericminio.json.JsonToMapsParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StubServer {
    private HttpServer server;
    private final List<Map<String, Object>> routes;

    public StubServer(String file) throws IOException {
        InputStream resource = this.getClass().getClassLoader().getResourceAsStream(file);
        BufferedReader data = new BufferedReader(new InputStreamReader(resource));
        String config = data.lines().collect(Collectors.joining());
        routes = (List<Map<String, Object>>) JsonToMapsParser.parse(config).get("routes");
    }

    public void start(int port) throws IOException {
        server = HttpServer.create( new InetSocketAddress( port ), 0 );
        server.createContext( "/", exchange -> {
            Map<String, Object> route = routes.get(0);
            Map<String, Object> answer = (Map<String, Object>) route.get("answer");
            exchange.getResponseHeaders().add( "Content-Type", (String) answer.get("contentType"));
            exchange.sendResponseHeaders((Integer) answer.get("statusCode"), 0 );
            exchange.getResponseBody().write( "{ \"alive\": true }".getBytes() );
            exchange.close();
        } );
        server.start();
    }

    public void stop() {
        server.stop( 1 );
    }


}

