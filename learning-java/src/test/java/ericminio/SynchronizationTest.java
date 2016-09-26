package ericminio;

import org.junit.Test;

import java.util.concurrent.CompletableFuture;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class SynchronizationTest {

    Step one = new Spy();
    Step two = new Spy();
    Workflow workflow = new Workflow(one, two);

    @Test
    public void isNeeded() throws Exception {
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> workflow.execute());
        workflow.execute();
        future.get();

        assertThat(Spy.firstCaller, not(equalTo(Spy.secondCaller)));
    }

    @Test
    public void works() throws Exception {
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> workflow.executeSynchronously());
        workflow.executeSynchronously();
        future.get();

        assertThat(Spy.firstCaller, equalTo(Spy.secondCaller));
    }

    static class Spy implements Step {

        public static int firstCaller;
        public static int secondCaller;

        public Spy() {
            firstCaller = -1;
            secondCaller = -1;
        }

        @Override
        public void execute() {
            if (firstCaller == -1) {
                firstCaller = Thread.currentThread().hashCode();
            }
            else {
                if (secondCaller == -1) {
                    secondCaller = Thread.currentThread().hashCode();
                }
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
