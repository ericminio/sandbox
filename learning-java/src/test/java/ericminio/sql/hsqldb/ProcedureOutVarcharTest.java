package ericminio.sql.hsqldb;

import org.junit.Before;
import org.junit.Test;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ProcedureOutVarcharTest extends DatabaseTest {

    @Before
    public void createRoutine() throws SQLException {
        execute("" +
                "create procedure greetings(input varchar(15), out output varchar(15)) \n" +
                "begin atomic \n" +
                "   set output = 'hello ' + input; \n" +
                "end;"
        );
    }

    @Test
    public void withCallableStatement() throws SQLException {
        CallableStatement statement = connection.prepareCall("call greetings(?, ?)");
        statement.registerOutParameter(2, Types.VARCHAR);
        statement.setString(1, "Joe");
        statement.execute();
        String actual = statement.getString(2);

        assertThat(actual, equalTo("hello Joe"));
    }
}
