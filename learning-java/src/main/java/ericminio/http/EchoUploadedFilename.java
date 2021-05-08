package ericminio.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ericminio.support.Stringify;

import java.io.IOException;

public class EchoUploadedFilename implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String incomingBody = new Stringify().inputStream(exchange.getRequestBody());
        String answer = new UploadPayloadParser().parse(incomingBody).get(0).getName();
        exchange.sendResponseHeaders( 200, answer.length() );
        exchange.getResponseBody().write( answer.getBytes() );
        exchange.close();
    }
}
