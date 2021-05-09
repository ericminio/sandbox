package ericminio.http;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;

public class FormDataProtocolTest {

    private FormDataProtocol formDataProtocol;

    @Before
    public void sut() {
        formDataProtocol = new FormDataProtocol();
    }

    @Test
    public void canParseFormData() {
        String incomingBody = "" +
                "-----token\n" +
                "Content-Disposition:form-data;name=field\n" +
                "\n" +
                "any content\n" +
                "-----token--\n";
        FormDataSet formDataSet = formDataProtocol.parse(incomingBody);
        assertThat(formDataSet.size(), equalTo(1));
        assertThat(formDataSet.get(0).getName(), equalTo("field"));
        assertThat(formDataSet.get(0).getValue(), equalTo("any content"));
    }

    @Test
    public void canParseFileInfo() {
        String incomingBody = "" +
                "-----token\n" +
                "Content-Disposition:form-data;name=field;filename=hello.txt\n" +
                "Content-Type:text/plain\n" +
                "\n" +
                "any content\n" +
                "-----token--\n";
        FormDataSet set = formDataProtocol.parse(incomingBody);
        assertThat(set.size(), equalTo(1));
        assertThat(set.get(0), instanceOf(FileFormData.class));
        FileFormData fileFormData = (FileFormData) set.get(0);
        assertThat(fileFormData.getFileName(), equalTo("hello.txt"));
        assertThat(fileFormData.getName(), equalTo("field"));
        assertThat(fileFormData.getContent(), equalTo("any content"));
    }

    @Test
    public void canExtractMultilineFileContent() {
        String incomingBody = "" +
                "-----token\n" +
                "Content-Disposition:form-data;name=field-name;filename=any.txt\n" +
                "Content-Type:text/plain\n" +
                "\n" +
                "any multi line\n" +
                "content\n" +
                "-----token--\n";
        FormDataSet set = formDataProtocol.parse(incomingBody);
        assertThat(set.size(), equalTo(1));

        FileFormData fileFormData = (FileFormData) set.get(0);
        assertThat(fileFormData.getContent(), equalTo("" +
                "any multi line\n" +
                "content"));
    }

    @Test
    public void canParseTwoFiles() {
        String incomingBody = "" +
                "-----token\n" +
                "Content-Disposition:form-data;name=one;filename=one.txt\n" +
                "Content-Type:text/plain\n" +
                "\n" +
                "one content\n" +
                "-----token\n" +
                "Content-Disposition:form-data;name=two;filename=two.txt\n" +
                "Content-Type:text/plain\n" +
                "\n" +
                "two content\n" +
                "-----token--\n";
        FormDataSet set = formDataProtocol.parse(incomingBody);
        assertThat(set.size(), equalTo(2));

        FileFormData first = (FileFormData) set.get(0);
        assertThat(first.getFileName(), equalTo("one.txt"));
        assertThat(first.getName(), equalTo("one"));
        assertThat(first.getValue(), equalTo("one content"));
        FileFormData second = (FileFormData) set.get(1);
        assertThat(second.getFileName(), equalTo("two.txt"));
        assertThat(second.getName(), equalTo("two"));
        assertThat(second.getValue(), equalTo("two content"));
    }

    @Test
    public void supportExtendedLineEndSeparator() {
        String incomingBody = "" +
                "-----token\r\n" +
                "Content-Disposition:form-data;name=one;filename=one.txt\r\n" +
                "Content-Type:text/plain\r\n" +
                "\r\n" +
                "one content\r\n" +
                "-----token\r\n" +
                "Content-Disposition:form-data;name=two;filename=two.txt\r\n" +
                "Content-Type:text/plain\r\n" +
                "\r\n" +
                "two content\r\n" +
                "-----token--\r\n";
        FormDataSet set = formDataProtocol.parse(incomingBody);
        assertThat(set.size(), equalTo(2));

        FileFormData first = (FileFormData) set.get(0);
        assertThat(first.getFileName(), equalTo("one.txt"));
        assertThat(first.getName(), equalTo("one"));
        assertThat(first.getValue(), equalTo("one content"));
        FileFormData second = (FileFormData) set.get(1);
        assertThat(second.getFileName(), equalTo("two.txt"));
        assertThat(second.getName(), equalTo("two"));
        assertThat(second.getValue(), equalTo("two content"));
    }

    @Test
    public void resistsFirefox() {
        String incomingBody = "" +
                "-----------------------------191462173837912382272006659563\n" +
                "Content-Disposition: form-data; name=\"one\"; filename=\"hello.txt\"\n" +
                "Content-Type: text/plain\n" +
                "\n" +
                "hello\n" +
                "\n" +
                "-----------------------------191462173837912382272006659563--\n";
        FormDataSet set = formDataProtocol.parse(incomingBody);
        assertThat(set.size(), equalTo(1));

        FileFormData fileFormData = (FileFormData) set.get(0);
        assertThat(fileFormData.getFileName(), equalTo("hello.txt"));
        assertThat(fileFormData.getName(), equalTo("one"));
        assertThat(fileFormData.getValue(), equalTo("hello"));
    }

    @Test
    public void resistsChrome() {
        String incomingBody = "" +
                "------WebKitFormBoundarySZQKHDCpRCnNKWS5\n" +
                "Content-Disposition: form-data; name=\"one\"; filename=\"hello.txt\"\n" +
                "Content-Type: text/plain\n" +
                "\n" +
                "hello\n" +
                "\n" +
                "------WebKitFormBoundarySZQKHDCpRCnNKWS5--\n";
        FormDataSet set = formDataProtocol.parse(incomingBody);
        assertThat(set.size(), equalTo(1));

        FileFormData fileFormData = (FileFormData) set.get(0);
        assertThat(fileFormData.getFileName(), equalTo("hello.txt"));
        assertThat(fileFormData.getName(), equalTo("one"));
        assertThat(fileFormData.getValue(), equalTo("hello"));
    }
}
