package ericminio.http;

import ericminio.support.Stringify;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class PutRequest {

    public static HttpResponse put(String url, String data) throws Exception {
        return put(url, new HashMap<>(), data.getBytes());
    }

    public static HttpResponse put(String url, byte[] data) throws Exception {
        return put(url, new HashMap<>(), data);
    }

    public static HttpResponse put(String url, Map<String, String> headers, byte[] data) throws Exception {
        HttpURLConnection request = (HttpURLConnection) new URL( url ).openConnection();
        request.setDoOutput(true);
        request.setRequestMethod("PUT");
        request.setRequestProperty( "Content-Type", "application/json");
        request.setRequestProperty( "Content-Length", Integer.toString(data.length));
        for (String header: headers.keySet()) {
            request.setRequestProperty( header, headers.get(header));
        }
        request.getOutputStream().write(data);

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
