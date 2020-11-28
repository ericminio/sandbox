package ericminio.files;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class FilesTest {

    @Test
    public void readWrite() throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.of(2015, Month.JANUARY, 15, 19, 15);
        String message = dateTime.format(formatter) + " [FLASH] Beauty";
        Path path = Paths.get("./target/read-write-test.log");
        Files.write(path, message.getBytes());
        String line = Files.readAllLines(path).get(0);

        assertThat(line, equalTo("2015-01-15 19:15:00 [FLASH] Beauty"));
    }

    @Test
    public void readLines() throws IOException {
        Path path = Paths.get("./src/test/java/ericminio/katas/mastermind/mastermind.yop");
        String line = Files.readAllLines(path).get(0);

        assertThat(line, equalTo("The mastermind..."));
    }

    @Test
    public void currentPackage() {
        assertThat(this.getClass().getPackage().getName(), equalTo("ericminio.files"));
    }
}
