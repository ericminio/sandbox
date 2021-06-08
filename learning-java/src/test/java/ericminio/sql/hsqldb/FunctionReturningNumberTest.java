package ericminio.sql.hsqldb;

import org.junit.Before;
import org.junit.Test;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class FunctionReturningNumberTest extends DatabaseTest {

    @Before
    public void createRoutine() throws SQLException {
        executeIgnoringErrors("drop function fortyTwo");
        execute("" +
                "create function fortyTwo() returns integer \n" +
                "begin atomic \n" +
                "   return 42; \n" +
                "end;"
        );
    }

    @Test
    public void withPreparedStatement() throws SQLException {
        PreparedStatement statement = connection.prepareStatement("call fortyTwo()");
        ResultSet resultSet = statement.executeQuery();
        resultSet.next();
        int actual = resultSet.getInt(1);

        assertThat(actual, equalTo(42));
    }
}
