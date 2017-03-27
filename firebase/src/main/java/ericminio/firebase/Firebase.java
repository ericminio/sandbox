package ericminio.firebase;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public class Firebase {

    private String url;

    public Firebase(String url) {
        this.url = url;
    }

    public void save(Entry entry) throws Exception {
        URL postUrl = new URL( this.url + entry.reference + ".json" );
        HttpURLConnection postConnection = (HttpURLConnection) postUrl.openConnection();
        postConnection.setDoOutput(true);
        postConnection.setRequestMethod("PUT");

        byte[] postData = entry.data.getBytes( StandardCharsets.UTF_8 );
        postConnection.setRequestProperty( "Content-Length", Integer.toString(postData.length));
        postConnection.setRequestProperty("Content-Type", "application/json");
        DataOutputStream writer = new DataOutputStream( postConnection.getOutputStream());
        writer.write(postData);

        int code = postConnection.getResponseCode();
        if (code != 200) {
            throw new RuntimeException("save failed with code " + code);
        }
    }

    public void delete(String reference) throws Exception {
        URL postUrl = new URL( this.url + reference + ".json" );
        HttpURLConnection postConnection = (HttpURLConnection) postUrl.openConnection();
        postConnection.setDoOutput(true);
        postConnection.setRequestMethod("DELETE");

        int code = postConnection.getResponseCode();
        if (code != 200) {
            throw new RuntimeException("delate failed with code " + code);
        }
    }

    public String read(String reference) throws Exception {
        URL getUrl = new URL( this.url + reference + ".json" );
        HttpURLConnection getConnection = (HttpURLConnection) getUrl.openConnection();

        InputStream inputStream = getConnection.getInputStream();
        BufferedReader responseBuffer = new BufferedReader(new InputStreamReader(inputStream));
        String response = responseBuffer.lines().collect(Collectors.joining());

        return response;
    }
}
