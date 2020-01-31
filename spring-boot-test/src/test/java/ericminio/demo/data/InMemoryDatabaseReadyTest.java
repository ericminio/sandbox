package ericminio.demo.data;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@SpringBootTest(
        properties= {
                "spring.profiles.active=database"
        }
)
@RunWith(SpringRunner.class)
public class InMemoryDatabaseReadyTest {

    @Value("${hibernate.dialect}")
    private String dialect;

    @Autowired
    JdbcTemplate template;

    @Test
    public void inMemoryDatabaseConfigurationIsReady() {
        assertThat(dialect, equalTo("org.hibernate.dialect.HSQLDialect"));
    }

    @Test
    public void databaseIsReady() {
        Integer one = template.queryForObject("SELECT 1 FROM INFORMATION_SCHEMA.SYSTEM_USERS", Integer.class);

        assertThat(one, equalTo(1));
    }
}
