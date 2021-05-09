package ericminio.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ericminio.support.Stringify;
import ericminio.zip.FileSet;

import java.io.IOException;

public class EchoUploadedFilename implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String incomingBody = new Stringify().inputStream(exchange.getRequestBody());
        FormDataSet formDataSet = new FormDataProtocol().parse(incomingBody);
        FileSet fileSet = new BuildFileSet().from(formDataSet);
        String answer = fileSet.get(0).getFileName();
        exchange.sendResponseHeaders( 200, answer.length() );
        exchange.getResponseBody().write( answer.getBytes() );
        exchange.close();
    }
}
