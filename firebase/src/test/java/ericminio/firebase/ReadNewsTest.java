package ericminio.firebase;

import ericminio.firebase.Entry;
import ericminio.firebase.Firebase;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class ReadNewsTest {

    private Firebase database;
    private String timestamp;
    private String data;
    private String url = "https://sandbox-5f095.firebaseio.com/";

    @Before
    public void saveOneEntry() throws Exception {
        database = new Firebase(url);

        timestamp = ""+System.currentTimeMillis();
        data = "{\"message\":\"hello Sunday "+ timestamp +"\",\"timestamp\":"+timestamp+"}";
        database.save(new Entry("news/"+ timestamp, data));
    }

    @Test
    public void isPossibleViaGETMethod() throws Exception {

        URL getUrl = new URL( url + "news.json?orderBy=\"timestamp\"&limitToLast=1" );
        HttpURLConnection getConnection = (HttpURLConnection) getUrl.openConnection();

        InputStream inputStream = getConnection.getInputStream();
        BufferedReader responseBuffer = new BufferedReader(new InputStreamReader(inputStream));
        String response = responseBuffer.lines().collect(Collectors.joining());

        assertThat( response, equalTo( "{\""+timestamp+"\":" + data + "}" ) );
    }
}
