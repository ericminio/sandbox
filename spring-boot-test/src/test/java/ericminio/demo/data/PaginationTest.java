package ericminio.demo.data;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@SpringBootTest(
        properties= {
                "spring.profiles.active=database"
        }
)
@RunWith(SpringRunner.class)
public class PaginationTest {

    @Autowired
    AnyEntityRepository repository;

    @Before
    public void truncateTable() {
        repository.deleteAll();
    }

    @Test
    public void works() {
        repository.save(entityWithField("a"));
        repository.save(entityWithField("b"));

        int pageSize = 10;
        int page = 0;
        List<AnyEntity> entities = repository.findByFieldIsNotNullOrderByField(PageRequest.of(page, pageSize));
        assertThat(entities.size(), equalTo(2));

        pageSize = 1;
        page = 0;
        entities = repository.findByFieldIsNotNullOrderByField(PageRequest.of(page, pageSize));
        assertThat(entities.size(), equalTo(1));
        assertThat(entities.get(0).getField(), equalTo("a"));

        page = 1;
        entities = repository.findByFieldIsNotNullOrderByField(PageRequest.of(page, pageSize));
        assertThat(entities.size(), equalTo(1));
        assertThat(entities.get(0).getField(), equalTo("b"));
    }

    @Test
    public void rerunsQueryForEachPage() {
        repository.save(entityWithField("b"));
        repository.save(entityWithField("c"));

        int pageSize = 1;

        int page = 0;
        List<AnyEntity> entities = repository.findByFieldIsNotNullOrderByField(PageRequest.of(page, pageSize));
        assertThat(entities.get(0).getField(), equalTo("b"));

        repository.save(entityWithField("a"));

        page = 1;
        entities = repository.findByFieldIsNotNullOrderByField(PageRequest.of(page, pageSize));
        assertThat(entities.get(0).getField(), equalTo("b"));
    }

    private AnyEntity entityWithField(final String field) {
        AnyEntity entity = new AnyEntity();
        entity.setField(field);

        return entity;
    }
}
