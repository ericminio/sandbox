package ericminio.testing;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import ericminio.json.JsonToMapsParser;
import ericminio.json.MapsToJsonParser;

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
    private final List<Route> routes;

    public StubServer(String file) {
        InputStream resource = this.getClass().getClassLoader().getResourceAsStream(file);
        BufferedReader data = new BufferedReader(new InputStreamReader(resource));
        String config = data.lines().collect(Collectors.joining());
        routes = ((List<Map<String, Object>>) JsonToMapsParser.parse(config).get("routes"))
                .stream().map(e -> new Route(e)).collect(Collectors.toList());
    }

    public void start(int port) throws IOException {
        server = HttpServer.create( new InetSocketAddress( port ), 0 );
        server.createContext( "/", exchange -> {
            Answer answer = routes.stream().filter(route -> route.isOpen(exchange))
                    .findFirst().get().getAnswer();
            byte[] body = answer.getBody();
            exchange.getResponseHeaders().add( "Content-Type", answer.getContentType());
            exchange.sendResponseHeaders(answer.getStatusCode(), body.length );
            exchange.getResponseBody().write(body);
            exchange.close();
        } );
        server.start();
    }

    public void stop() {
        server.stop( 1 );
    }

    class Route {
        Map<String, Object> definition;

        public Route(Map<String, Object> definition) {
            this.definition = definition;
        }

        public boolean isOpen(HttpExchange exchange) {
            return getGate().isOpen(exchange);
        }

        public Gate getGate() {
            return new Gate((Map<String, Object>) this.definition.get("when"));
        }

        public Answer getAnswer() {
            return new Answer((Map<String, Object>) this.definition.get("answer"));
        }
    }
    class Gate {
        Map<String, Object> definition;

        public Gate(Map<String, Object> definition) {
            this.definition = definition;
        }

        public boolean isOpen(HttpExchange exchange) {
            if (this.definition == null) { return true; }

            String expectedUrlPrefix = (String) this.definition.get("urlStartsWith");
            if (expectedUrlPrefix != null && !exchange.getRequestURI().toString().startsWith(expectedUrlPrefix)) { return false; }

            String expectedMethod = (String) this.definition.get("methodIs");
            if (expectedMethod != null && !exchange.getRequestMethod().equalsIgnoreCase(expectedMethod)) { return false; }

            return true;
        }
    }
    class Answer {
        Map<String, Object> definition;

        public Answer(Map<String, Object> definition) {
            this.definition = definition;
        }

        public String getContentType() {
            return (String) this.definition.get("contentType");
        }

        public Integer getStatusCode() {
            return (Integer) this.definition.get("statusCode");
        }

        public byte[] getBody() {
            return MapsToJsonParser.stringify(this.definition.get("body")).getBytes();
        }
    }
}

