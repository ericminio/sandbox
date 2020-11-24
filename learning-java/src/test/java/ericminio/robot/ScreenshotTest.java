package ericminio.robot;

import org.junit.Before;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class ScreenshotTest {

    private Robot robot;

    @Before
    public void sut() throws AWTException {
        robot = new Robot();
    }

    @Test
    public void size() {
        BufferedImage image = robot.createScreenCapture(new Rectangle(0, 0, 150, 100));

        assertThat(image.getWidth(), equalTo(150));
        assertThat(image.getHeight(), equalTo(100));
    }

    @Test
    public void diskRoundTrip() throws IOException {
        int width = 150;
        int height = 100;
        BufferedImage image = robot.createScreenCapture(new Rectangle(0, 0, width, height));
        int rgb = image.getRGB(15, 15);

        File f = new File("target/image.png");
        ImageIO.write(image, "PNG", f);

        BufferedImage read = ImageIO.read(f);
        assertThat(read.getWidth(), equalTo(width));
        assertThat(read.getHeight(), equalTo(height));
        assertThat(read.getRGB(15, 15), equalTo(rgb));
    }
}
