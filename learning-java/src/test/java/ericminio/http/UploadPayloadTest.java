package ericminio.http;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class UploadPayloadTest {

    @Test
    public void findByFieldName() {
        FileInfo one = new FileInfo();
        one.setFieldName("one-field");
        one.setFileName("one-name");
        FileInfo two = new FileInfo();
        two.setFieldName("two-field");
        two.setFileName("two-name");
        UploadPayload uploadPayload = new UploadPayload();
        uploadPayload.add(one);
        uploadPayload.add(two);

        assertThat(uploadPayload.getFileInfoByFieldName("one-field").getFileName(), equalTo("one-name"));
    }
}
