package ericminio.workflow;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class SynchronizationTest {

    private Step one;
    private Step two;
    private Workflow workflow;

    @Before
    public void given() {
        one = new Spy();
        two = new Spy();
        workflow = new Workflow();
        workflow.add(one);
        workflow.add(two);
    }

    @Test
    public void isNeeded() throws Exception {
        CompletableFuture<Void> firstCall = CompletableFuture.runAsync(() -> workflow.execute());
        CompletableFuture<Void> secondCall = CompletableFuture.runAsync(() -> workflow.execute());
        firstCall.get();
        secondCall.get();

        assertThat(Spy.firstCaller, not(equalTo(Spy.secondCaller)));
    }

    @Test
    public void works() throws Exception {
        CompletableFuture<Void> firstCall = CompletableFuture.runAsync(() -> workflow.executeSynchronously());
        CompletableFuture<Void> secondCall = CompletableFuture.runAsync(() -> workflow.executeSynchronously());
        firstCall.get();
        secondCall.get();

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
                Thread.sleep(150);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void cancel() {

        }
    }
}
