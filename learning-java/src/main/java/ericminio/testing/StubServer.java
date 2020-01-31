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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class StubServer {
    private HttpServer server;
    private List<Route> routes;
    private Map<String, Object> variables;
    private Map<String, StubServer.Function> functions;

    public StubServer(String configFile) {
        this(configFile, new HashMap<>(), new HashMap<>());
    }

    public StubServer(String configFile, Map<String, Object> variables, Map<String, StubServer.Function> functions) {
        this.variables = variables;
        this.functions = functions;
        InputStream resource = this.getClass().getClassLoader().getResourceAsStream(configFile);
        String config = new BufferedReader(new InputStreamReader(resource)).lines().collect(Collectors.joining());
        routes = ((List<Map<String, Object>>) JsonToMapsParser.parse(config).get("routes"))
                .stream().map(e -> new Route(e)).collect(Collectors.toList());
    }

    public void start(int port) throws IOException {
        server = HttpServer.create( new InetSocketAddress( port ), 0 );
        server.createContext( "/", exchange -> {
            Incoming incoming = new Incoming(exchange, variables);
            Answer answer = routes.stream().filter(route -> route.isOpen(incoming))
                    .findFirst().get().getAnswer();
            String body = evaluate(answer.getBody(), incoming);
            exchange.getResponseHeaders().add( "Content-Type", answer.getContentType());
            exchange.sendResponseHeaders(answer.getStatusCode(), body.length() );
            exchange.getResponseBody().write(body.getBytes());
            exchange.close();
        } );
        server.start();
    }

    private String evaluate(String body, Incoming incoming) {
        String evaluated = body;
        for (String key :variables.keySet()) {
            evaluated = evaluated.replaceAll("#"+key, variables.get(key).toString());
        }
        for (String key :functions.keySet()) {
            Function function = functions.get(key);
            evaluated = evaluated.replaceAll("~"+key+"\\(\\)", function.execute(incoming, variables));
        }
        return evaluated;
    }

    public void stop() {
        server.stop( 1 );
    }

    class Route {
        Map<String, Object> definition;

        public Route(Map<String, Object> definition) {
            this.definition = definition;
        }

        public boolean isOpen(Incoming incoming) {
            return getGate().isOpen(incoming);
        }

        public Gate getGate() {
            return new Gate((Map<String, Object>) this.definition.get("when"));
        }

        public Answer getAnswer() {
            return new Answer((Map<String, Object>) this.definition.get("answer"));
        }
    }
    class Incoming {

        private String uri;
        private String method;
        private String body;
        private HttpExchange exchange;
        private Map<String, Object> variables;

        public Incoming(HttpExchange exchange, Map<String, Object> variables) {
            this.exchange = exchange;
            this.variables = variables;

            this.uri = exchange.getRequestURI().toString();
            this.method = exchange.getRequestMethod().toUpperCase();
            this.body = new BufferedReader(new InputStreamReader(exchange.getRequestBody())).lines().collect(Collectors.joining());
        }

        public String getUri() {
            return this.uri;
        }

        public String getMethod() {
            return this.method;
        }

        public String getBody() {
            return this.body;
        }

        public boolean hasUriStartingWith(String expectedUrlPrefix) {
            return this.getUri().startsWith(expectedUrlPrefix);
        }

        public boolean hasUriMatching(String expectedUrlMatch) {
            Pattern pattern = Pattern.compile(expectedUrlMatch);
            Matcher matcher = pattern.matcher(this.getUri());
            if (matcher.matches()) {
                this.variables.put("groupCount", matcher.groupCount());
                for (int i = 1; i <= matcher.groupCount(); i++) {
                    this.variables.put("group-"+i, matcher.group(i));
                }
                return true;
            }
            else {
                return false;
            }
        }

        public boolean hasMethod(String expectedMethod) {
            return this.getMethod().equalsIgnoreCase(expectedMethod);
        }

        public boolean hasBodyContaining(String expectedBody) {
            return this.getBody().contains(expectedBody);
        }
    }
    class Gate {
        private Map<String, Object> definition;

        public Gate(Map<String, Object> definition) {
            this.definition = definition;
        }

        public boolean isOpen(Incoming incoming) {
            if (this.definition == null) { return true; }

            String expectedUrlPrefix = (String) this.definition.get("urlStartsWith");
            if (expectedUrlPrefix != null && !incoming.hasUriStartingWith(expectedUrlPrefix)) { return false; }

            String expectedMethod = (String) this.definition.get("methodIs");
            if (expectedMethod != null && !incoming.hasMethod(expectedMethod)) { return false; }

            String expectedBody = (String) this.definition.get("bodyContains");
            if (expectedBody != null && !incoming.hasBodyContaining(expectedBody)) { return false; }

            String expectedUrlMatch = (String) this.definition.get("urlMatches");
            if (expectedUrlMatch != null && !incoming.hasUriMatching(expectedUrlMatch)) { return false; }

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

        public String getBody() {
            Object body = this.definition.get("body");
            if (body instanceof String) { return body.toString(); }

            return MapsToJsonParser.stringify(body);
        }
    }

    interface Function {
        String execute(Incoming incoming, Map<String, Object> variables);
    }
}
