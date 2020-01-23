package ericminio;

import org.junit.Test;

import java.time.LocalDateTime;
import java.time.Month;
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
}
