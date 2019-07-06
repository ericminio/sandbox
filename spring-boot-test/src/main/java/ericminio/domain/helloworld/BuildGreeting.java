package ericminio.domain.helloworld;

import org.springframework.stereotype.Component;

@Component
public class BuildGreeting {

    private String name;

    public BuildGreeting from(String name) {
        this.name = name;
        return this;
    }

    public Greeting please() {
        Greeting greeting = new Greeting();
        greeting.content = String.format(Greeting.TEMPLATE, this.name);

        return greeting;
    }
}
