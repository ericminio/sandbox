package ericminio.firebase;


import org.junit.Test;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class CreateNewsTest {

    @Test
    public void isPossibleViaPUTMethod() throws Exception {
        String timestamp = ""+System.currentTimeMillis();
        String data = "{\"message\":\"hello Sunday "+ timestamp +"\",\"timestamp\":"+timestamp+"}";
        String reference = "news/"+ timestamp +".json";

        URL url = new URL( "https://sandbox-5f095.firebaseio.com/" + reference );
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("PUT");

        byte[] postData = data.getBytes( StandardCharsets.UTF_8 );
        connection.setRequestProperty( "Content-Length", Integer.toString(postData.length));
        connection.setRequestProperty("Content-Type", "application/json");
        DataOutputStream writer = new DataOutputStream( connection.getOutputStream());
        writer.write(postData);

        assertThat( connection.getResponseCode(), equalTo( 200 ) );

        InputStream inputStream = connection.getInputStream();
        BufferedReader responseBuffer = new BufferedReader(new InputStreamReader(inputStream));
        String response = responseBuffer.lines().collect(Collectors.joining());

        assertThat( response, equalTo( data ) );
    }
}
