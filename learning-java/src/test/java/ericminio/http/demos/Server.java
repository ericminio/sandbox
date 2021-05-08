package ericminio.http.demos;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Server {

    private int port = 8005;
    private HttpServer httpServer;

    public static void main(String[] args) {
        try {
            Server server = new Server();
            server.start();
        }
        catch (Exception raised) {
            raised.printStackTrace();
        }
    }

    public void start() throws IOException {
        httpServer = HttpServer.create( new InetSocketAddress( port ), 0 );
        httpServer.createContext( "/", new Index());
        httpServer.createContext( "/explore", new Explore());
        httpServer.start();
    }
}
