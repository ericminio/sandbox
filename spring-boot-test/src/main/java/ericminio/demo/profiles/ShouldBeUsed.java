package ericminio.demo.profiles;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("visible")
public class ShouldBeUsed implements Stuff {

    @Override
    public String getName() {
        return "good";
    }
}
