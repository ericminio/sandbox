package ericminio.domain;

import java.io.Serializable;
import java.util.regex.Pattern;

public class Greeting implements Serializable {

    public static final String TEMPLATE = "Hello, %s!";
    public static final Pattern PATTERN = Pattern.compile(
            TEMPLATE
                .replace(" ", "\\s")
                .replace("%s", "(.*)")
    );

    public String content;
}
