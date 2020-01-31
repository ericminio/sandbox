package ericminio.demo.data;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@SpringBootTest(
        properties= {
                "spring.profiles.active=database"
        }
)
@RunWith(SpringRunner.class)
public class CustomQueries {

    @Autowired
    AnyEntityRepository repository;

    @Test
    public void canDigestParameters() {
        AnyEntity anyEntity = new AnyEntity();
        anyEntity.setField("42");
        repository.save(anyEntity);

        assertThat(repository.countWhereFieldIs("42"), equalTo(1));
        assertThat(repository.countWhereFieldIs("55"), equalTo(0));
    }
}
