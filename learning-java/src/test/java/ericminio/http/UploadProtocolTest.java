package ericminio.http;

import ericminio.zip.FileInfo;
import ericminio.zip.FileSet;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class UploadProtocolTest {

    private UploadProtocol uploadProtocol;

    @Before
    public void sut() {
        uploadProtocol = new UploadProtocol();
    }

    @Test
    public void canExtractFilename() {
        String incomingBody = "" +
                "-----token\n" +
                "Content-Disposition:form-data;name=file;filename=any.txt\n" +
                "Content-Type:application/octet-stream\n" +
                "\n" +
                "any content\n" +
                "-----token--\n";
        FileSet fileSet = uploadProtocol.parse(incomingBody);
        assertThat(fileSet.size(), equalTo(1));

        FileInfo fileInfo = fileSet.getFileInfo(0);
        assertThat(fileInfo.getFileName(), equalTo("any.txt"));
    }

    @Test
    public void canExtractFieldName() {
        String incomingBody = "" +
                "-----token\n" +
                "Content-Disposition:form-data;name=field-name;filename=any.txt\n" +
                "Content-Type:application/octet-stream\n" +
                "\n" +
                "any content\n" +
                "-----token--\n";
        FileSet fileSet = uploadProtocol.parse(incomingBody);
        assertThat(fileSet.size(), equalTo(1));

        FileInfo fileInfo = fileSet.getFileInfo(0);
        assertThat(fileInfo.getFieldName(), equalTo("field-name"));
    }

    @Test
    public void canExtractFileContent() {
        String incomingBody = "" +
                "-----token\n" +
                "Content-Disposition:form-data;name=field-name;filename=any.txt\n" +
                "Content-Type:application/octet-stream\n" +
                "\n" +
                "any multi line\n" +
                "content\n" +
                "-----token--\n";
        FileSet fileSet = uploadProtocol.parse(incomingBody);
        assertThat(fileSet.size(), equalTo(1));

        FileInfo fileInfo = fileSet.getFileInfo(0);
        assertThat(fileInfo.getContent(), equalTo("" +
                "any multi line\n" +
                "content"));
    }

    @Test
    public void canParseTwoFiles() {
        String incomingBody = "" +
                "-----token\n" +
                "Content-Disposition:form-data;name=one;filename=one.txt\n" +
                "Content-Type:application/octet-stream\n" +
                "\n" +
                "one content\n" +
                "-----token\n" +
                "Content-Disposition:form-data;name=two;filename=two.txt\n" +
                "Content-Type:application/octet-stream\n" +
                "\n" +
                "two content\n" +
                "-----token--\n";
        FileSet fileSet = uploadProtocol.parse(incomingBody);
        assertThat(fileSet.size(), equalTo(2));

        assertThat(fileSet.getFileInfo(0).getFileName(), equalTo("one.txt"));
        assertThat(fileSet.getFileInfo(0).getFieldName(), equalTo("one"));
        assertThat(fileSet.getFileInfo(0).getContent(), equalTo("one content"));
        assertThat(fileSet.getFileInfo(1).getFileName(), equalTo("two.txt"));
        assertThat(fileSet.getFileInfo(1).getFieldName(), equalTo("two"));
        assertThat(fileSet.getFileInfo(1).getContent(), equalTo("two content"));
    }

    @Test
    public void supportExtendedLineEndSeparator() {
        String incomingBody = "" +
                "-----token\r\n" +
                "Content-Disposition:form-data;name=one;filename=one.txt\r\n" +
                "Content-Type:application/octet-stream\r\n" +
                "\r\n" +
                "one content\r\n" +
                "-----token\r\n" +
                "Content-Disposition:form-data;name=two;filename=two.txt\r\n" +
                "Content-Type:application/octet-stream\r\n" +
                "\r\n" +
                "two content\r\n" +
                "-----token--\r\n";
        FileSet fileSet = uploadProtocol.parse(incomingBody);
        assertThat(fileSet.size(), equalTo(2));

        assertThat(fileSet.getFileInfo(0).getFileName(), equalTo("one.txt"));
        assertThat(fileSet.getFileInfo(0).getFieldName(), equalTo("one"));
        assertThat(fileSet.getFileInfo(0).getContent(), equalTo("one content"));
        assertThat(fileSet.getFileInfo(1).getFileName(), equalTo("two.txt"));
        assertThat(fileSet.getFileInfo(1).getFieldName(), equalTo("two"));
        assertThat(fileSet.getFileInfo(1).getContent(), equalTo("two content"));
    }
}
