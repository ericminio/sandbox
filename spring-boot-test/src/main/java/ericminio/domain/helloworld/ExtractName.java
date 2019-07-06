package ericminio.domain.helloworld;

import org.springframework.stereotype.Component;

import java.util.regex.Matcher;

@Component
public class ExtractName {

    private Greeting greeting;

    public ExtractName from(Greeting greeting) {
        this.greeting = greeting;
        return this;
    }

    public String please() {
        Matcher matcher = Greeting.PATTERN.matcher(this.greeting.content);
        matcher.find();

        return matcher.group(1);
    }
}
