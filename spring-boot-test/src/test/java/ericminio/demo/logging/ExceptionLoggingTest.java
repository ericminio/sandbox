package ericminio.demo.logging;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static ericminio.support.ClearFile.clearFile;
import static ericminio.support.FileContent.contentOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

public class ExceptionLoggingTest {

    String filename = "logs/42.log";

    @Before
    public void cleanStart() {
        try { clearFile(filename); }
        catch (IOException e) {}
    }

    @Test
    public void isAvailable() throws IOException {
        try {
            throw new RuntimeException("something bad happened");
        }
        catch (RuntimeException e) {
            Logger logger = LoggerFactory.getLogger(XmlConfigurationTest.class);
            logger.error("oops", e);

            assertThat(contentOf(filename), containsString("[ExceptionLoggingTest] - oops"));
            assertThat(contentOf(filename), containsString("java.lang.RuntimeException: something bad happened"));
            assertThat(contentOf(filename), containsString("at ericminio.demo.logging.ExceptionLoggingTest.isAvailable"));
        }
    }
}
