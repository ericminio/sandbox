package ericminio.workflow;

public interface Step {

    void execute();
    void cancel();
}
