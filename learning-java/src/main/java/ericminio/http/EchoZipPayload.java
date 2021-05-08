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
        FileSet fileSet = new UploadProtocol().parse(new Stringify().inputStream(exchange.getRequestBody()));
        byte[] bytes = zip(fileSet);
        exchange.sendResponseHeaders(200, bytes.length);
        exchange.getResponseBody().write(bytes);
        exchange.close();
    }

    private byte[] zip(FileSet fileSet) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream, StandardCharsets.UTF_8);
        for (int i = 0; i< fileSet.size(); i++) {
            FileInfo fileInfo = fileSet.getFileInfo(i);
            ZipEntry zipEntry = new ZipEntry(fileInfo.getFileName());
            zipOutputStream.putNextEntry(zipEntry);
            zipOutputStream.write(fileInfo.getContent().getBytes(), 0, fileInfo.getContent().length());
            zipOutputStream.flush();
            zipOutputStream.closeEntry();
        }
        zipOutputStream.finish();
        zipOutputStream.close();

        return byteArrayOutputStream.toByteArray();
    }
}
