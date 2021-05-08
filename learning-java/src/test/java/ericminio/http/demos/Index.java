package ericminio.http.demos;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ericminio.support.Stringify;

import java.io.IOException;
import java.io.InputStream;

public class Index implements HttpHandler {

    private final String page;

    public Index() throws IOException {
        InputStream stream = this.getClass().getClassLoader().getResourceAsStream("index.html");
        this.page = new Stringify().inputStream(stream);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().add( "content-type", "text/html" );
        exchange.sendResponseHeaders(200, this.page.length());
        exchange.getResponseBody().write(this.page.getBytes());
        exchange.close();
    }
}
