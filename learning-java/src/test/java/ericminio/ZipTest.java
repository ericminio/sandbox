package ericminio;

import ericminio.support.Stringify;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ZipTest {

    private String fileName = "target/explore.zip";
    @Before
    public void clean() {
        safeDelete(fileName);
    }

    private void safeDelete(String fileName) {
        try {
            new File(fileName).delete();
        }
        catch (Exception ignored) {}
    }

    @Test
    public void roundTripOnDisk() throws IOException {
        String content = "hello world";
        FileOutputStream fileOutputStream = new FileOutputStream(fileName);
        ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream, StandardCharsets.UTF_8);
        ZipEntry zipEntry = new ZipEntry("hello.txt");
        zipOutputStream.putNextEntry(zipEntry);
        zipOutputStream.write(content.getBytes(), 0, content.length());
        zipOutputStream.flush();
        zipOutputStream.closeEntry();
        zipOutputStream.finish();
        zipOutputStream.close();

        FileInputStream fileInputStream = new FileInputStream(fileName);
        ZipInputStream zipInputStream = new ZipInputStream(fileInputStream);
        ZipEntry entry = zipInputStream.getNextEntry();
        assertThat(entry.getName(), equalTo("hello.txt"));

        String actual = new Stringify().inputStream(zipInputStream);
        assertThat(actual, equalTo("hello world"));
        assertThat(zipInputStream.available(), equalTo(0));

        zipInputStream.closeEntry();
        assertThat(zipInputStream.getNextEntry(), equalTo(null));

        zipInputStream.close();
    }
}
