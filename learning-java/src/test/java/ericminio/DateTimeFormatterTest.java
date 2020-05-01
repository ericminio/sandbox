package ericminio;

import org.junit.Test;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class DateTimeFormatterTest {

    @Test
    public void isAvailable() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime dateTime = LocalDateTime.of(2015, Month.DECEMBER, 15, 0, 0);

        assertThat(dateTime.format(formatter), equalTo("2015-12-15"));
    }
    @Test
    public void zuluTime() {
        ZonedDateTime local = LocalDateTime.of(2015, Month.JANUARY, 15, 19, 15).atZone(ZoneId.of("UTC-6"));
        ZonedDateTime utc = ZonedDateTime.ofInstant(local.toInstant(), ZoneId.of("UTC"));

        assertThat(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXX").format(utc), equalTo("2015-01-16T01:15:00.000Z"));
    }
}
