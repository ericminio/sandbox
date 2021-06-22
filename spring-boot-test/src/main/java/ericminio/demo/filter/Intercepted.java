package ericminio.demo.filter;

import org.springframework.stereotype.Component;

@Component
public class Intercepted {
    private String value;

    public Intercepted() {
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
