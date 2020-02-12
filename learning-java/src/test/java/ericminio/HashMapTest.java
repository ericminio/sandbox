package ericminio;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class HashMapTest {

    @Test
    public void acceptsNullKey() {
        Map<String, Object> map = new HashMap();

        assertThat(map.size(), equalTo(0));
        assertThat(map.get("field"), equalTo(null));

        map.put("field", null);

        assertThat(map.size(), equalTo(1));
        assertThat(map.get("field"), equalTo(null));
    }
}
