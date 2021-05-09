package ericminio.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ericminio.support.Stringify;

import java.io.IOException;

public class EchoUploadToken implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String incomingBody = new Stringify().inputStream(exchange.getRequestBody());
        String token = incomingBody.substring(0, incomingBody.indexOf(FormDataProtocol.end));
        exchange.sendResponseHeaders( 200, token.length() );
        exchange.getResponseBody().write( token.getBytes() );
        exchange.close();
    }
}
