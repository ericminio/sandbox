package ericminio.demo.chat;

import ericminio.demo.chat.domain.Exclusion;
import ericminio.demo.chat.domain.Group;
import ericminio.demo.chat.domain.Person;
import ericminio.demo.chat.http.Data;
import ericminio.support.Csrf;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpMethod.POST;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= RANDOM_PORT)
public class ExclusionServiceTest {

    @Autowired
    Csrf csrf;

    @LocalServerPort
    int port;

    private String apply;

    @Before
    public void buildEndpoint() {
        apply = "http://localhost:"+ port +"/apply";
    }

    @Test
    public void works() {
        Group group = new Group(new Person("Diana"), new Person("Joe"), new Person("Jenny"));
        Exclusion exclusion = new Exclusion(new Person("Diana"), new Person("Jenny"));
        Data data = new Data(group, exclusion);

        HttpEntity<Data> request = new HttpEntity<>(data, csrf.headers());

        TestRestTemplate restTemplate = new TestRestTemplate("user", "correct-password");
        ResponseEntity<Group> response = restTemplate.exchange(apply, POST, request, Group.class);
        Group expected = new Group(new Person("Diana"), new Person("Joe"));

        assertThat( response.getStatusCode(), equalTo( HttpStatus.OK ) );
        assertThat( response.getBody(), equalTo( expected ) );
    }
}
