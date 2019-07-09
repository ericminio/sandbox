package ericminio.demo.helloworld.domain;

import java.io.Serializable;
import java.util.regex.Pattern;

public class Greeting implements Serializable {

    public static final String TEMPLATE = "Hello, %s!";
    public static final Pattern PATTERN = Pattern.compile(TEMPLATE
            .replace(" ", "\\s")
            .replace("%s", "(.*)")
    );

    public String content;

    public boolean equals(Object o) {
        if (! (o instanceof Greeting)) { return false; }
        Greeting other = (Greeting) o;

        return other.content.equalsIgnoreCase(this.content);
    }

    public String toString() {
        return this.getClass().getName() + ":" + this.content;
    }
}
