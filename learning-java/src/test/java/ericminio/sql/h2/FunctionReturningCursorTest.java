package ericminio.sql.h2;

import org.h2.tools.SimpleResultSet;
import org.junit.Before;
import org.junit.Test;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class FunctionReturningCursorTest extends DatabaseTest {

    @Before
    public void createRoutine() throws SQLException {
        executeIgnoringErrors("drop alias test.someValues");
        executeIgnoringErrors("create schema test");
        execute("create alias test.someValues for \"ericminio.sql.h2.FunctionReturningCursorTest.data\"");
    }

    public static ResultSet data() {
        SimpleResultSet rs = new SimpleResultSet();
        rs.addColumn("ID", Types.INTEGER, 10, 0);
        rs.addColumn("NAME", Types.VARCHAR, 255, 0);
        rs.addRow(15, "Hello");
        rs.addRow(42, "World");
        return rs;
    }

    @Test
    public void withPreparedStatement() throws SQLException {
        PreparedStatement statement = connection.prepareStatement("call test.someValues()");
        ResultSet resultSet = statement.executeQuery();
        resultSet.next();
        resultSet.next();

        assertThat(resultSet.getString(2), equalTo("World"));
    }

    @Test
    public void withCallableStatementAndResultSet() throws SQLException {
        CallableStatement statement = connection.prepareCall("{? = call test.someValues() }");
        statement.execute();
        ResultSet resultSet = statement.getResultSet();
        resultSet.next();
        resultSet.next();

        assertThat(resultSet.getString(2), equalTo("World"));
    }

    @Test
    public void withCallableStatementAndSelect() throws SQLException {
        CallableStatement statement = connection.prepareCall("select * from test.someValues()");
        statement.execute();
        ResultSet resultSet = statement.getResultSet();
        resultSet.next();
        resultSet.next();

        assertThat(resultSet.getString(2), equalTo("World"));
    }
}
