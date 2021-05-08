package ericminio.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ericminio.support.Stringify;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class EchoZipPayload implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String incomingBody = new Stringify().inputStream(exchange.getRequestBody());
        UploadPayload payload = new UploadProtocol().parse(incomingBody);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream, StandardCharsets.UTF_8);
        FileInfo fileInfo = payload.getFileInfo(0);
        ZipEntry zipEntry = new ZipEntry(fileInfo.getFileName());
        zipOutputStream.putNextEntry(zipEntry);
        zipOutputStream.write(fileInfo.getContent().getBytes(), 0, fileInfo.getContent().length());
        zipOutputStream.flush();
        zipOutputStream.closeEntry();
        zipOutputStream.finish();
        zipOutputStream.close();
        byte[] bytes = byteArrayOutputStream.toByteArray();

        exchange.sendResponseHeaders( 200, bytes.length );
        exchange.getResponseBody().write( bytes );
        exchange.close();
    }
}
