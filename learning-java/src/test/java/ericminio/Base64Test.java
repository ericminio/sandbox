package ericminio;

import org.junit.Test;

import java.util.Base64;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.assertThat;

public class Base64Test {

    @Test
    public void canEncode() {
        assertThat(Base64.getEncoder().encodeToString("hello".getBytes()), equalTo("aGVsbG8="));
    }

    @Test
    public void canDecode() {
        byte[] decoded = Base64.getDecoder().decode("aGVsbG8=");

        assertThat(new String(decoded), equalTo("hello"));
        assertThat(new String(new String(decoded).getBytes()), equalTo("hello"));
    }

    @Test
    public void canBeMisleading() {
        byte[] decoded = Base64.getDecoder().decode("aGVsbG8=");

        assertThat(String.valueOf(decoded), startsWith("[B@"));
    }
}
