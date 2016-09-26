package ericminio;

public class Workflow {

    private final Step stepOne;
    private final Step stepTwo;

    public Workflow(Step one, Step two) {
        this.stepOne = one;
        this.stepTwo = two;
    }

    public void execute() {
        stepOne.execute();
        stepTwo.execute();
    }

    public synchronized void executeSynchronously() {
        stepOne.execute();
        stepTwo.execute();
    }
}
