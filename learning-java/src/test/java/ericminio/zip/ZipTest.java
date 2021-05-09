package ericminio.zip;

import ericminio.http.FileInfo;
import ericminio.http.FileSet;
import ericminio.support.Stringify;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ZipTest {

    @Test
    public void createsExpectedZipEntries() throws IOException {
        FileSet fileSet = new FileSet();
        fileSet.add(new FileInfo("field1", "one.txt", "hello world"));
        fileSet.add(new FileInfo("field2", "two.txt", "hello world"));
        byte[] zip = new Zip().please(fileSet);

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(zip);
        ZipInputStream zipInputStream = new ZipInputStream(byteArrayInputStream, StandardCharsets.UTF_8);
        assertThat(zipInputStream.getNextEntry().getName(), equalTo("one.txt"));
        assertThat(zipInputStream.getNextEntry().getName(), equalTo("two.txt"));
        assertThat(zipInputStream.getNextEntry(), equalTo(null));

        zipInputStream.close();
    }

    @Test
    public void populatesEntriesWithExpectedContent() throws IOException {
        FileSet fileSet = new FileSet();
        fileSet.add(new FileInfo("field1", "one.txt", "content #1"));
        fileSet.add(new FileInfo("field2", "two.txt", "content #2"));
        byte[] zip = new Zip().please(fileSet);

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(zip);
        ZipInputStream zipInputStream = new ZipInputStream(byteArrayInputStream, StandardCharsets.UTF_8);

        zipInputStream.getNextEntry();
        ZipEntry entry = zipInputStream.getNextEntry();
        assertThat(entry.getName(), equalTo("two.txt"));

        String actual = new Stringify().inputStream(zipInputStream);
        assertThat(actual, equalTo("content #2"));
        assertThat(zipInputStream.available(), equalTo(0));

        zipInputStream.close();
    }
}
