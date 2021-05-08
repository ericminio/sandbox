package ericminio.http;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class UploadPayloadParserTest {

    @Test
    public void canExtractFilename() {
        String incomingBody = "" +
                "-----token\n" +
                "Content-Disposition:form-data;name=file;filename=any.txt\n" +
                "Content-Type:application/octet-stream\n" +
                "\n" +
                "any content\n" +
                "-----token--";
        UploadPayloadParser uploadPayloadParser = new UploadPayloadParser();
        UploadPayload uploadPayload = uploadPayloadParser.parse(incomingBody);
        assertThat(uploadPayload.size(), equalTo(1));

        UploadedFile uploadedFile = uploadPayload.get(0);
        assertThat(uploadedFile.getName(), equalTo("any.txt"));
    }

    @Test
    public void canExtractFieldName() {
        String incomingBody = "" +
                "-----token\n" +
                "Content-Disposition:form-data;name=field-name;filename=any.txt\n" +
                "Content-Type:application/octet-stream\n" +
                "\n" +
                "any content\n" +
                "-----token--";
        UploadPayloadParser uploadPayloadParser = new UploadPayloadParser();
        UploadPayload uploadPayload = uploadPayloadParser.parse(incomingBody);
        assertThat(uploadPayload.size(), equalTo(1));

        UploadedFile uploadedFile = uploadPayload.get(0);
        assertThat(uploadedFile.getFieldName(), equalTo("field-name"));
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
                "-----token--";
        UploadPayloadParser uploadPayloadParser = new UploadPayloadParser();
        UploadPayload uploadPayload = uploadPayloadParser.parse(incomingBody);
        assertThat(uploadPayload.size(), equalTo(1));

        UploadedFile uploadedFile = uploadPayload.get(0);
        assertThat(uploadedFile.getContent(), equalTo("" +
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
                "-----token--";
        UploadPayloadParser uploadPayloadParser = new UploadPayloadParser();
        UploadPayload uploadPayload = uploadPayloadParser.parse(incomingBody);
        assertThat(uploadPayload.size(), equalTo(2));

        assertThat(uploadPayload.get(0).getName(), equalTo("one.txt"));
        assertThat(uploadPayload.get(0).getFieldName(), equalTo("one"));
        assertThat(uploadPayload.get(0).getContent(), equalTo("one content"));
        assertThat(uploadPayload.get(1).getName(), equalTo("two.txt"));
        assertThat(uploadPayload.get(1).getFieldName(), equalTo("two"));
        assertThat(uploadPayload.get(1).getContent(), equalTo("two content"));
    }
}
