package ericminio.demo.profiles;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("hidden")
public class ShouldNotBeUsed implements Stuff {

    @Override
    public String getName() {
        return "not good";
    }
}
