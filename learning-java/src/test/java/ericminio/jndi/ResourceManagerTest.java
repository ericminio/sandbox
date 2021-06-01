package ericminio.jndi;

import org.junit.Test;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ResourceManagerTest {

    @Test
    public void readsJndiPropertiesFile() throws NamingException {
        InitialContext context = new InitialContext();

        assertThat(context.lookup("greetings"), equalTo("hello world"));
    }
}
