package ericminio.robot;

import org.junit.Test;

import java.awt.*;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class GraphicsEnvironmentTest {

    @Test
    public void headlessFlag() {
        assertThat(GraphicsEnvironment.isHeadless(), equalTo(false));
    }

    @Test
    public void instance() {
        assertThat(GraphicsEnvironment.getLocalGraphicsEnvironment(), not(equalTo(null)));
    }

    @Test
    public void defaultDevice() {
        assertThat(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice(), notNullValue());
    }

    @Test
    public void screen() {
        GraphicsDevice screenDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

        assertThat(screenDevice.getType(), equalTo(GraphicsDevice.TYPE_RASTER_SCREEN));
    }

    @Test
    public void bounds() {
        GraphicsDevice screenDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        GraphicsConfiguration defaultConfiguration = screenDevice.getDefaultConfiguration();
        Rectangle bounds = defaultConfiguration.getBounds();

        assertThat(bounds.getX(), equalTo(0.0));
        assertThat(bounds.getY(), equalTo(0.0));
    }
}
