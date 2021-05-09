package ericminio.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ericminio.support.Stringify;

import java.io.IOException;

public class EchoFormNames implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        FormDataSet formDataSet = new FormDataProtocol().parse(new Stringify().inputStream(exchange.getRequestBody()));
        String answer = "";
        for (int i=0; i<formDataSet.size(); i++) {
            answer += (formDataSet.get(i).getName() + " ");
        }
        exchange.getResponseHeaders().add( "content-type", "text/plain" );
        exchange.sendResponseHeaders(200, answer.length());
        exchange.getResponseBody().write(answer.getBytes());
        exchange.close();
    }
}
