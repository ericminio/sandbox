package ericminio.demo.data;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@SpringBootTest(
        properties= {
                "spring.profiles.active=database"
        }
)
@RunWith(SpringRunner.class)
public class NamedQueryTest {

    @Autowired
    EntityManager entityManager;
    @Autowired
    AnyEntityRepository repository;

    @Before
    public void truncateTable() {
        repository.deleteAll();
    }

    @Test
    public void isOneWayToQueryAView() {
        AnyEntity anyEntity = new AnyEntity();
        anyEntity.setField("star");
        repository.save(anyEntity);
        anyEntity = new AnyEntity();
        anyEntity.setField("star");
        repository.save(anyEntity);

        List<Report> records = entityManager.createNamedQuery("GroupByValue", Report.class).getResultList();

        assertThat(records.size(), equalTo(1));
        assertThat(records.get(0).getValue(), equalTo("star"));
        assertThat(records.get(0).getCount(), equalTo(2));

    }
}
