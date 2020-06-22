package ericminio.workflow;

import java.util.ArrayList;
import java.util.Collections;

public class Workflow {

    private ArrayList<Step> steps;

    public Workflow() {
        steps = new ArrayList();
    }

    public void add(Step step) {
        steps.add(step);
    }

    public void execute() {
        ArrayList<Step> maybeCancel = new ArrayList();
        try {
            for (Step step : steps) {
                step.execute();
                maybeCancel.add(step);
            }
        }
        catch (Exception e) {
            Collections.reverse(maybeCancel);
            for (Step step:maybeCancel) {
                step.cancel();
            }
        }
    }

    public synchronized void executeSynchronously() {
        execute();
    }
}
