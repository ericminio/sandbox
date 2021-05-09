package ericminio.http;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class FormDataSetTest {

    @Test
    public void getByName() {
        FormDataSet set = new FormDataSet();
        set.add(new FormData("this-field", "this-value"));
        set.add(new FormData("that-field", "that-value"));

        assertThat(set.getByName("that-field").getValue(), equalTo("that-value"));
    }
}
