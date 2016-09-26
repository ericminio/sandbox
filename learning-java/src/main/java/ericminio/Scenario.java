package ericminio;

public class Scenario {

    private final Step stepOne;
    private final Step stepTwo;

    public Scenario(Step one, Step two) {
        this.stepOne = one;
        this.stepTwo = two;
    }

    public void go() {
        stepOne.execute();
        stepTwo.execute();
    }

    public synchronized void goTheSynchronizedWay() {
        stepOne.execute();
        stepTwo.execute();
    }
}
