package ericminio.demo.configuration;

import ericminio.domain.Something;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Java6Assertions.assertThat;

/**
 * Created by perso on 2017-06-22.
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {SharableConfiguration.class})
public class SharableConfigurationTest {

    @Autowired
    Something something;

    @Test
    public void configurationCanInjectNativeBean() {
        assertThat(something).isNotNull();
    }

    @Test
    public void injectedInstanceIsTheExpectedOne() {
        assertThat(something.getName()).isEqualTo("shared");
    }
}
