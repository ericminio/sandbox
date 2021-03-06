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
    private int port;
    private JsonRouter router;
    private Map<String, Object> variables;
    private Map<String, JsonRouter.Function> functions;
    private WaitStrategy waitStrategy;

    public ServerStub(String configFile) {
        this(configFile, new HashMap<>(), new HashMap<>());
    }

    public ServerStub(String configFile, Map<String, Object> variables, Map<String, JsonRouter.Function> functions) {
        setRoutes(configFile, variables, functions);
        setWaitStrategy(new WaitStrategyUsingGetEndpoint("/is-ready"));
    }

    public void setRoutes(String configFile) {
        setRoutes(configFile, new HashMap<>(), new HashMap<>());
    }

    public void setRoutes(String configFile, Map<String, Object> variables, Map<String, JsonRouter.Function> functions) {
        InputStream resource = this.getClass().getClassLoader().getResourceAsStream(configFile);
        String config = new BufferedReader(new InputStreamReader(resource)).lines().collect(Collectors.joining());
        router = new JsonRouter(config, variables, functions);
        router.insertRoute("/is-ready", 200, "text/plain", "yep");
    }

    public void start(int port) throws IOException {
        this.port = port;
        server = HttpServer.create( new InetSocketAddress( port ), 0 );
        server.createContext( "/", exchange -> {
            try {
                JsonRouter.Answer answer = router.digest(exchange);
                if (answer.isDelayed()) {
                    try {
                        Thread.sleep(answer.getDelay());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                exchange.getResponseHeaders().add("Content-Type", answer.getContentType());
                exchange.sendResponseHeaders(answer.getStatusCode(), answer.getEvaluateBody().length());
                exchange.getResponseBody().write(answer.getEvaluateBody().getBytes());
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            exchange.close();
        });
        server.start();
        this.getWaitStrategy().waitForStarted(port);
    }

    public void stop() {
        if (server != null) {
            server.stop(0);
            this.getWaitStrategy().waitForStopped(this.port);
        }
    }

    public Map<String, Object> getVariables() {
        return router.getVariables();
    }

    public Map<String, JsonRouter.Function> getFunctions() {
        return router.getFunctions();
    }

    public WaitStrategy getWaitStrategy() {
        return waitStrategy;
    }

    public void setWaitStrategy(WaitStrategy waitStrategy) {
        this.waitStrategy = waitStrategy;
    }
}

