package ericminio.sql;

import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class DaoTest {

    Dao dao;

    @Before
    public void sut() {
        dao = new Dao();
    }

    @Test
    public void exposesFindAllValues() throws SQLException {
        dao.setRepository(() -> new ResultSetUsingStaticData(Arrays.asList(
                Arrays.asList("hello"),
                Arrays.asList("world")
        )));
        List<String> values = dao.findAll();

        assertThat(values.size(), equalTo(2));
    }
}
