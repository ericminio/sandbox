package ericminio.concurrency;

import org.junit.Test;

import java.util.concurrent.*;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class DelayQueueTest {

    @Test
    public void returnsImmediatelyWhenDelayIsZero() throws InterruptedException {
        DelayQueue queue = new DelayQueue();
        queue.put(new DelayedByGivenMilliseconds(0, "unique"));
        DelayedByGivenMilliseconds element = (DelayedByGivenMilliseconds) queue.take();

        assertThat(element.getData(), equalTo("unique"));
    }
    @Test
    public void waitsForGivenDelayToExpireBeforeReturning() throws InterruptedException {
        BlockingQueue<DelayedByGivenMilliseconds> queue = new DelayQueue();

        long before = System.currentTimeMillis();
        queue.put(new DelayedByGivenMilliseconds( 150, "unique"));
        queue.take();
        long after = System.currentTimeMillis();

        assertThat((int)(after - before), greaterThan(150));
    }
    @Test
    public void canBePopulatedFromAnotherThread() throws Exception {
        DelayQueue queue = new DelayQueue();
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(2);
        scheduledExecutorService.schedule(() -> {
            try {
                DelayedByGivenMilliseconds taken = (DelayedByGivenMilliseconds) queue.take();
                taken.setData("taken");
            } catch (InterruptedException e) {
                fail(e.getMessage());
            }
        }, 15, TimeUnit.MILLISECONDS);
        DelayedByGivenMilliseconds element = new DelayedByGivenMilliseconds(150, "unique");
        scheduledExecutorService.schedule(() -> {
            queue.put(element);
        }, 150, TimeUnit.MILLISECONDS);
        scheduledExecutorService.awaitTermination(1, TimeUnit.SECONDS);
        scheduledExecutorService.shutdown();

        assertThat(element.getData(), equalTo("taken"));
    }
    @Test
    public void keepsNextElementToExpireFirstInTheList() throws InterruptedException {
        DelayQueue queue = new DelayQueue();
        queue.put(new DelayedByGivenMilliseconds(300, "300"));
        queue.put(new DelayedByGivenMilliseconds(150, "150"));
        DelayedByGivenMilliseconds element = (DelayedByGivenMilliseconds) queue.take();

        assertThat(element.getData(), equalTo("150"));
    }

    class DelayedByGivenMilliseconds implements Delayed {

        private final long endTime;
        private String data;

        public DelayedByGivenMilliseconds(long delay, String value) {
            this.data = value;
            this.endTime = System.currentTimeMillis() + delay;
        }
        public String toString() {
            return this.getClass().getSimpleName() + ":" + this.data;
        }

        @Override
        public long getDelay(TimeUnit unit) {
            long delay = endTime - System.currentTimeMillis();
            return unit.convert(delay, TimeUnit.MILLISECONDS);
        }

        @Override
        public int compareTo(Delayed o) {
            return (int) (endTime - ((DelayedByGivenMilliseconds) o).endTime);
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }
}
