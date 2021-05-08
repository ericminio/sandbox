package ericminio.http;

import ericminio.zip.FileInfo;
import ericminio.zip.FileSet;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class FileSetTest {

    @Test
    public void findByFieldName() {
        FileInfo one = new FileInfo();
        one.setFieldName("one-field");
        one.setFileName("one-name");
        FileInfo two = new FileInfo();
        two.setFieldName("two-field");
        two.setFileName("two-name");
        FileSet fileSet = new FileSet();
        fileSet.add(one);
        fileSet.add(two);

        assertThat(fileSet.getFileInfoByFieldName("one-field").getFileName(), equalTo("one-name"));
    }
}
