package ericminio.katas.mastermind;

import ericminio.yop.Yop;
import ericminio.yop.YopTest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class ExploreYopTest extends YopTest {

    @Override
    protected String getYopFileName() {
        return "explore.yop";
    }

    @Yop(match = "the feedback is (.*)")
    public void expectFeedback(String feedback) {
        assertThat(feedback, equalTo("3, 0"));
    }

}
