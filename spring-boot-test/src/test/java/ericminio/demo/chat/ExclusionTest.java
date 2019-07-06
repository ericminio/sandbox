package ericminio.demo.chat;

import ericminio.domain.chat.Exclusion;
import ericminio.domain.chat.Group;
import ericminio.domain.chat.Person;
import ericminio.http.chat.Data;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpMethod.POST;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= RANDOM_PORT)
public class ExclusionTest {

    @LocalServerPort
    int port;

    private String apply;

    @Before
    public void buildEndpoint() {
        apply = "http://localhost:"+ port +"/apply";
    }

    @Test
    public void works() {
        Group group = new Group();
        group.setPersons(Arrays.asList(new Person("Diana"), new Person("Joe"), new Person("Jenny")));
        Exclusion exclusion = new Exclusion(new Person("Diana"), new Person("Jenny"));
        Data data = new Data(group, exclusion);

        HttpEntity<Data> request = new HttpEntity<>(data);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Group> response = restTemplate.exchange(apply, POST, request, Group.class);
        Group expected = new Group();
        expected.setPersons(Arrays.asList(new Person("Diana"), new Person("Joe")));

        assertThat( response.getBody(), equalTo( expected ) );
    }
}
