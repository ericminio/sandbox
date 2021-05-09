package ericminio.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ericminio.support.Stringify;

import java.io.IOException;

public class EchoUploadedFilenames implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String incomingBody = new Stringify().inputStream(exchange.getRequestBody());
        FormDataSet formDataSet = new FormDataProtocol().parse(incomingBody);
        FileSet fileSet = FileSet.from(formDataSet);
        String answer = "";
        for (int i=0; i<fileSet.size(); i++) {
            answer += fileSet.getFileInfo(i).getFileName() + " ";
        }
        exchange.sendResponseHeaders( 200, answer.length() );
        exchange.getResponseBody().write( answer.getBytes() );
        exchange.close();
    }
}
