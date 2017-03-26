package ericminio.firebase;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

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
}
