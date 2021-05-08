package ericminio.http;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class UploadPayloadTest {

    @Test
    public void findByFieldName() {
        UploadedFile one = new UploadedFile();
        one.setFieldName("one-field");
        one.setFileName("one-name");
        UploadedFile two = new UploadedFile();
        two.setFieldName("two-field");
        two.setFileName("two-name");
        UploadPayload uploadPayload = new UploadPayload();
        uploadPayload.add(one);
        uploadPayload.add(two);

        assertThat(uploadPayload.getFileByFieldName("one-field").getFileName(), equalTo("one-name"));
    }
}
