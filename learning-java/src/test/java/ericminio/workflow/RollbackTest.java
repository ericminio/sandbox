package ericminio.workflow;

import org.junit.Test;

import static org.mockito.Mockito.*;

public class RollbackTest {

    @Test
    public void stopsAtFirstError() {
        Workflow workflow = new Workflow();
        Step one = new Step() {
            @Override
            public void execute() {
                throw new RuntimeException("say execute #1 fails");
            }
            @Override
            public void cancel() {}
        };
        Step two = mock(Step.class);
        workflow.add(one);
        workflow.add(two);
        workflow.execute();
        verify(two, never()).execute();
    }

    @Test
    public void rollbacksThePassedSteps() {
        Workflow workflow = new Workflow();
        Step one = mock(Step.class);
        Step two = mock(Step.class);
        Step three = new Step() {
            @Override
            public void execute() {
                throw new RuntimeException("say execute #3 fails");
            }
            @Override
            public void cancel() {}
        };
        workflow.add(one);
        workflow.add(two);
        workflow.add(three);
        workflow.execute();
        verify(two).cancel();
        verify(one).cancel();
    }

    @Test
    public void stopsAtFirstCancellationError() {
        Workflow workflow = new Workflow();
        Step three = new Step() {
            @Override
            public void execute() {
                throw new RuntimeException("say execute #3 fails");
            }
            @Override
            public void cancel() {}
        };
        Step two = new Step() {
            @Override
            public void execute() {}
            @Override
            public void cancel() {
                throw new RuntimeException("say cancel #2 fails");
            }
        };
        Step one = mock(Step.class);
        workflow.add(one);
        workflow.add(two);
        workflow.add(three);
        try {
            workflow.execute();
        }
        catch (Exception e) {
            verify(one, never()).cancel();
        }
    }
}
