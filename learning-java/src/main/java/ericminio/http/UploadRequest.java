package ericminio.http;

import ericminio.support.Stringify;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static ericminio.http.UploadProtocol.*;

public class UploadRequest {

    public static HttpResponse upload(String url, String data) throws Exception {
        HttpURLConnection request = (HttpURLConnection) new URL( url ).openConnection();
        request.setDoOutput(true);
        request.setDoInput(true);
        request.setUseCaches(false);
        request.setRequestMethod("POST");
        request.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
        OutputStream outputStream = request.getOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

        dataOutputStream.writeBytes(hyphens + boundary + end);
        dataOutputStream.writeBytes("Content-Disposition:form-data;name=file;filename=any.txt" + end);
        dataOutputStream.writeBytes("Content-Type:application/octet-stream" + end);
        dataOutputStream.writeBytes(end);
        dataOutputStream.write(data.getBytes(), 0, data.getBytes().length);
        dataOutputStream.writeBytes(end);

        dataOutputStream.writeBytes(hyphens + boundary + twoHyphens + end);
        dataOutputStream.flush();

        HttpResponse response = new HttpResponse();
        response.setStatusCode(request.getResponseCode());
        response.setContentType(request.getContentType());
        if (request.getResponseCode() < 400) {
            response.setBody(new Stringify().inputStream(request.getInputStream()));
        } else {
            response.setBody(new Stringify().inputStream(request.getErrorStream()));
        }

        return response;
    }
}
