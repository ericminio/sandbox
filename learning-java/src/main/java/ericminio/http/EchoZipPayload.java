package ericminio.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ericminio.support.Stringify;
import ericminio.zip.Zip;

import java.io.IOException;

public class EchoZipPayload implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        FormDataSet formDataSet = new FormDataProtocol().parse(new Stringify().inputStream(exchange.getRequestBody()));
        FileSet fileSet = FileSet.from(formDataSet);
        byte[] bytes = new Zip().please(fileSet);
        exchange.getResponseHeaders().add( "content-type", "application/zip" );
        exchange.getResponseHeaders().add( "content-disposition", "attachment; filename=\"download.zip\"" );
        exchange.sendResponseHeaders(200, bytes.length);
        exchange.getResponseBody().write(bytes);
        exchange.close();
    }
}
