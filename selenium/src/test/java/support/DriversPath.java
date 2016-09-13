package support;

import org.junit.Test;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringEndsWith.endsWith;

public class DriversPath {

    @Test
    public void geckoDriverPathIsKnown() {
        Path path = Paths.get("selenium", "src", "test", "resources", "geckodriver.exe");

        assertThat(DriversPath.firefox(), endsWith(path.toString()));
    }

    @Test
    public void chromeDriverPathIsKnown() {
        Path path = Paths.get("selenium", "src", "test", "resources", "chromedriver.exe");

        assertThat(DriversPath.chrome(), endsWith(path.toString()));
    }

    public static String chrome() {
        File current = new File("");
        Path path = Paths.get("src", "test", "resources", "chromedriver.exe");

        return current.getAbsolutePath() + File.separator + path.toString();
    }

    public static String firefox() {
        File current = new File("");
        Path path = Paths.get("src", "test", "resources", "geckodriver.exe");

        return current.getAbsolutePath() + File.separator + path.toString();
    }
}
