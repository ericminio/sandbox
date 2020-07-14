package support;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class GetRequest {

    public static HttpResponse get(String url) throws Exception {
        return get(url, new HashMap<>());
    }

    public static HttpResponse get(String url, int readTimeout) throws Exception {
        return get(url, new HashMap<>(), readTimeout);
    }

    public static HttpResponse get(String url, Map<String, String> headers) throws Exception {
        return get(url, headers, 0);
    }

    public static HttpResponse get(String url, Map<String, String> headers, int readTimeout) throws Exception {
        HttpURLConnection request = (HttpURLConnection) new URL( url ).openConnection();
        request.setReadTimeout(readTimeout);
        for (String header: headers.keySet()) {
            request.setRequestProperty( header, headers.get(header));
        }
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
