package ericminio.http.demos;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Server {

    private int port;
    private HttpServer httpServer;

    public Server(int port) {
        this.port = port;
    }

    public void stop() {
        httpServer.stop(1);
    }

    public void start() throws IOException {
        httpServer = HttpServer.create( new InetSocketAddress( port ), 0 );
        httpServer.createContext( "/", new Index());
        httpServer.createContext( "/explore", new Explore());
        httpServer.start();
    }
}
