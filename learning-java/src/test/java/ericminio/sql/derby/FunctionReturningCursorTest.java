package ericminio.sql.derby;

import org.junit.Before;
import org.junit.Test;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class FunctionReturningCursorTest extends DatabaseTest {

    @Before
    public void createRoutine() throws SQLException {
        executeIgnoringErrors("drop table product");
        execute("create table product(name varchar(15))");
        execute("insert into product (name) values ('one')");
        execute("insert into product (name) values ('two')");
        execute("" +
                "create function any_function() \n" +
                "   returns TABLE(name varchar(15)) \n" +
                "   language java \n" +
                "   parameter style DERBY_JDBC_RESULT_SET \n" +
                "   external name 'ericminio.sql.derby.FunctionReturningCursorTest.read'"
        );
    }

    public static ResultSet read() throws SQLException {
        PreparedStatement statement = connection.prepareStatement("select name from product");
        return statement.executeQuery();
    }

    @Test
    public void withCallableStatementAndSelect() throws SQLException {
        CallableStatement statement = connection.prepareCall("select * from table( any_function() ) a");
        statement.execute();
        ResultSet resultSet = statement.getResultSet();
        resultSet.next();
        resultSet.next();

        assertThat(resultSet.getString(1), equalTo("two"));
    }
}
