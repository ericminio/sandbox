package ericminio;

import org.junit.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertThat;

public class CompareDateTimeTest {

    @Test
    public void isPossibleWithDuration() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime later = now.plusSeconds(2);
        Duration duration = Duration.between(now, later);

        assertThat(duration.toMillis(), greaterThan(0l));
        assertThat(duration.toMillis(), lessThan(3 * 1000l));
    }
}
