package ericminio.http;

import ericminio.support.Bytify;
import ericminio.support.Stringify;

import java.net.HttpURLConnection;
import java.net.URL;

public class UploadRequest {

    public static HttpResponse upload(String url, FileSet fileSet) throws Exception {
        HttpURLConnection request = (HttpURLConnection) new URL( url ).openConnection();
        request.setDoOutput(true);
        request.setDoInput(true);
        request.setUseCaches(false);
        request.setRequestMethod("POST");
        new FormDataProtocol().send(fileSet, request);

        HttpResponse response = new HttpResponse();
        response.setStatusCode(request.getResponseCode());
        response.setContentType(request.getContentType());
        response.setContentDisposition(request.getHeaderField("content-disposition"));
        if (request.getResponseCode() < 400) {
            byte[] bytes = new Bytify().inputStream(request.getInputStream());
            response.setBinaryBody(bytes);
            response.setBody(new String(bytes));
        } else {
            response.setBody(new Stringify().inputStream(request.getErrorStream()));
        }

        return response;
    }
}
