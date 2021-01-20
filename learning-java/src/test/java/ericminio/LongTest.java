package ericminio;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class LongTest {

    @Test
    public void offersHexadecimalConversion() {
        assertThat(Long.decode("0x1").longValue(), equalTo(1L));
        assertThat(Long.decode("0xA").longValue(), equalTo(10L));
        assertThat(Long.decode("0x10").longValue(), equalTo(16L));
        assertThat(Long.decode("0x92").longValue(), equalTo(146L));
        assertThat(Long.decode("0xA1").longValue(), equalTo(10*16 + 1L));
        assertThat(Long.decode("0x26A1EAE2").longValue(), equalTo(648145634L));
    }

    @Test
    public void supportsLeftShifting() {
        assertThat(Long.decode("0x1").longValue() << 1, equalTo(2L));
        assertThat(Long.decode("0x1").longValue() << 2, equalTo(4L));
        assertThat(Long.decode("0xA").longValue() << 1, equalTo(20l));
        assertThat(Long.decode("0x1").longValue() << 8, equalTo(256l));
    }
}
