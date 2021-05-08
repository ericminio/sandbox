package ericminio.http;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class UploadProtocol {
    public static String boundary = "token";
    public static String hyphens = "-----";
    public static String twoHyphens = "--";
    public static String end = "\n";
    private HttpURLConnection request;

    public UploadProtocol(HttpURLConnection request) {
        this.request = request;
    }

    public String getRequestContentType() {
        return "multipart/form-data;boundary=" + boundary;
    }

    public String getFileContentType() {
        return "Content-Type:application/octet-stream" + end;
    }

    public String fileStart() {
        return hyphens + boundary + end;
    }

    public static String fileEnd() {
        return end;
    }

    public String payloadEnd() {
        return hyphens + boundary + twoHyphens + end;
    }

    public String getFileContentDisposition(String fieldName, String fileName) {
        return "Content-Disposition:form-data;name="+fieldName+";filename=" + fileName + end;
    }

    public void send(String fieldName, String fileName, String content) throws IOException {
        request.setRequestProperty("Content-Type", getRequestContentType());
        OutputStream outputStream = request.getOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

        dataOutputStream.writeBytes(fileStart());
        dataOutputStream.writeBytes(getFileContentDisposition(fieldName, fileName));
        dataOutputStream.writeBytes(getFileContentType());
        dataOutputStream.writeBytes(end);
        dataOutputStream.write(content.getBytes(), 0, content.getBytes().length);
        dataOutputStream.writeBytes(fileEnd());

        dataOutputStream.writeBytes(payloadEnd());
        dataOutputStream.flush();
    }
}
