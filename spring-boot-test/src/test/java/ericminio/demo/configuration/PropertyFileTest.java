package ericminio.demo.configuration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@SpringBootTest(
        properties= {
                "spring.profiles.active=variables"
        }
)
@RunWith(SpringRunner.class)
public class PropertyFileTest {

    @Value("${variables.array}")
    private String[] array;

    @Value("${variables.arrayMultiline}")
    private String[] arrayMultiline;

    @Value("#{${variables.list}}")
    private List<String> list;

    @Value("#{${variables.listMultiline}}")
    private List<String> listMultiline;

    @Value("${variables.arrayResistsUris}")
    private String[] arrayResistsUris;

    @Test
    public void canReadArrayOfStrings() {
        assertThat(array, equalTo(new String[]{ "hello", "world" }));
    }
    @Test
    public void canReadArrayFromMultilineDeclaration() {
        assertThat(arrayMultiline, equalTo(new String[]{ "I", "see", "you" }));
    }
    @Test
    public void canReadListOfStrings() {
        assertThat(list, equalTo(Arrays.asList("welcome", "home")));
    }
    @Test
    public void canReadListOfStringsFromMultilineDeclaration() {
        assertThat(listMultiline, equalTo(Arrays.asList("make", "yourself", "comfortable")));
    }
    @Test
    public void canReadArrayOfStringsContainingUris() {
        assertThat(arrayResistsUris, equalTo(new String[]{ "/path/to/endpoint/one", "/path/to/endpoint/two" }));
    }
}
