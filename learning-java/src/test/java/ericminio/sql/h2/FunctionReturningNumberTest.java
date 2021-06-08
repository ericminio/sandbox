package ericminio.sql.h2;

import org.junit.Before;
import org.junit.Test;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class FunctionReturningNumberTest extends DatabaseTest {

    @Before
    public void createRoutine() throws SQLException {
        executeIgnoringErrors("drop alias test.fortyTwo");
        executeIgnoringErrors("create schema test");
        execute("" +
                "create alias test.fortyTwo as $$ \n" +
                "Integer fortyTwoInternal() { \n" +
                "   return 42; \n" +
                "}" +
                "$$;"
        );
    }

    @Test
    public void withPreparedStatement() throws SQLException {
        PreparedStatement statement = connection.prepareStatement("call test.fortyTwo()");
        ResultSet resultSet = statement.executeQuery();
        resultSet.next();
        int actual = resultSet.getInt(1);

        assertThat(actual, equalTo(42));
    }

    @Test
    public void withPreparedCall() throws SQLException {
        CallableStatement statement = connection.prepareCall("{? = call test.fortyTwo() }");
        statement.registerOutParameter(1, Types.NUMERIC);
        statement.execute();
        int actual = statement.getInt(1);

        assertThat(actual, equalTo(42));
    }
}
