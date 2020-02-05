package ericminio.testing;

import com.sun.net.httpserver.HttpServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ServerStub {
    private HttpServer server;
    private JsonRouter router;

    public ServerStub(String configFile) {
        this(configFile, new HashMap<>(), new HashMap<>());
    }

    public ServerStub(String configFile, Map<String, Object> variables, Map<String, JsonRouter.Function> functions) {
        InputStream resource = this.getClass().getClassLoader().getResourceAsStream(configFile);
        String config = new BufferedReader(new InputStreamReader(resource)).lines().collect(Collectors.joining());
        router = new JsonRouter(config, variables, functions);
    }

    public void start(int port) throws IOException {
        server = HttpServer.create( new InetSocketAddress( port ), 0 );
        server.createContext( "/", exchange -> {
            JsonRouter.Answer answer = router.digest(exchange);
            exchange.getResponseHeaders().add( "Content-Type", answer.getContentType());
            exchange.sendResponseHeaders(answer.getStatusCode(), answer.getEvaluateBody().length() );
            exchange.getResponseBody().write(answer.getEvaluateBody().getBytes());
            exchange.close();
        });
        server.start();
    }

    public void stop() {
        server.stop( 1 );
    }

    public Map<String, Object> getVariables() {
        return router.getVariables();
    }

    public Map<String, JsonRouter.Function> getFunctions() {
        return router.getFunctions();
    }
}

