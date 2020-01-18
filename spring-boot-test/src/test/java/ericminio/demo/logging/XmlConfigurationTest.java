package ericminio.demo.logging;

import ericminio.support.ClearFile;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static ericminio.support.ClearFile.clearFile;
import static ericminio.support.FileContent.contentOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

public class XmlConfigurationTest {

    String filename = "logs/42.log";

    @Before
    public void cleanStart() throws IOException {
        clearFile(filename);
    }

    @Test
    public void allowPatternDefinition() throws IOException {
        Logger logger = LoggerFactory.getLogger(XmlConfigurationTest.class);
        logger.info("hello");

        assertThat(contentOf(filename), containsString("[XmlConfigurationTest] - hello"));
    }
}
