package ericminio.sql.hsqldb;

import org.junit.Before;
import org.junit.Test;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ProcedureOutNumberTest extends DatabaseTest {

    @Before
    public void createRoutine() throws SQLException {
        execute("" +
                "create procedure plus(input integer, out output integer) \n" +
                "begin atomic \n" +
                "   set output = input+1; \n" +
                "end;"
        );
    }

    @Test
    public void withCallableStatement() throws SQLException {
        CallableStatement statement = connection.prepareCall("call plus(?, ?)");
        statement.registerOutParameter(2, Types.INTEGER);
        statement.setInt(1, 41);
        statement.execute();
        int actual = statement.getInt(2);

        assertThat(actual, equalTo(42));
    }
}
