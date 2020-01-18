package ericminio.demo.logging;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.classic.spi.LoggerContextListener;
import ericminio.support.ClearFile;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static ericminio.support.ClearFile.clearFile;
import static ericminio.support.FileContent.contentOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

public class JavaConfigurationTest {

    String filename = "logs/43.log";

    @Before
    public void cleanStart() {
        try { clearFile(filename); }
        catch (IOException e) {}
    }

    @Test
    public void allowDefinitionOverwrite() throws Exception {
        System.setProperty("LOG_FILE", filename);

        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        JoranConfigurator configurator = new JoranConfigurator();
        configurator.setContext(context);
        configurator.doConfigure(this.getClass().getClassLoader().getResourceAsStream("logback.xml"));
        Logger logger = context.getLogger(JavaConfigurationTest.class);
        logger.info("coucou");

        assertThat(contentOf(filename), containsString("** JavaConfigurationTest ** - coucou"));
    }
}
