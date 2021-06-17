package ericminio.demo.filter;

import org.springframework.stereotype.Component;

@Component
public class Filtered {
    private String value;

    public Filtered() {
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
