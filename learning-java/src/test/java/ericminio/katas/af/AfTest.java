package ericminio.katas.af;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class AfTest {

    ApplicationFirewall af;

    @Before
    public void sut() {
        af = new ApplicationFirewallUsingSemaphores();
    }

    @Test
    public void resourceIsAsOpenAsTheNumberOfPermits() {
        AfConfiguration configuration = new AfConfiguration();
        configuration.setPermits(2);
        af.setConfiguration(configuration);

        assertThat(af.isOpen(3), equalTo(true));
        assertThat(af.isOpen(3), equalTo(true));
        assertThat(af.isOpen(3), equalTo(false));
    }
}
