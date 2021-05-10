package ericminio.zip;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class FileSetTest {

    @Test
    public void getByFileName() {
        FileSet fileSet = new FileSet();
        fileSet.add(new FileInfo("name-1.txt", "content #1"));
        fileSet.add(new FileInfo("name-2.txt", "content #2"));

        assertThat(fileSet.getByFileName("name-2.txt").getContent(), equalTo("content #2"));
    }
}
