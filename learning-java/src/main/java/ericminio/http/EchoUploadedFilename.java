package ericminio.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ericminio.support.Stringify;

import java.io.IOException;

public class EchoUploadedFilename implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String incomingBody = new Stringify().inputStream(exchange.getRequestBody());
        String answer = new UploadProtocol().parse(incomingBody).getFileInfo(0).getFileName();
        exchange.sendResponseHeaders( 200, answer.length() );
        exchange.getResponseBody().write( answer.getBytes() );
        exchange.close();
    }
}
