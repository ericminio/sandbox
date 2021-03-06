package ericminio.testing;

import org.junit.Test;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class ScheduledFutureTest {

    @Test
    public void canBeUsedToPostponeExecution() throws Exception {
        Tops tops = new Tops();
        ScheduledFuture<?> future = Executors.newScheduledThreadPool(1).schedule(() -> {
            try {
                tops.tic();
            } catch (Exception e) {
                fail(e.getMessage());
            }

        }, 2, TimeUnit.SECONDS);
        future.get();
        int delay = tops.delay();

        assertThat(delay, greaterThanOrEqualTo(2000));
        assertThat(delay, lessThan(3000));
    }
    class Tops {
        private long before;
        private long after;

        public Tops(){
            this.before = System.currentTimeMillis();
        }

        public void tic() {
            this.after = System.currentTimeMillis();
        }

        public int delay() {
            return (int) (after - before);
        }
    }
}
