package ericminio.demo.logging;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ericminio.support.FileContent.lastLineOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

public class DefaultConfigurationTest {

    @Test
    public void worksAsExpected() throws Exception {
        Logger logger = LoggerFactory.getLogger(DefaultConfigurationTest.class);
        logger.debug("this goes to default log");

        assertThat(lastLineOf(   "logs/default.log"), containsString("DEBUG [DefaultConfigurationTest] - this goes to default log"));
    }
}
