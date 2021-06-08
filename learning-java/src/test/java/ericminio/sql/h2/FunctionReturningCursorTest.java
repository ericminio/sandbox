package ericminio.sql.h2;

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
        executeIgnoringErrors("drop alias test.someValues");
        executeIgnoringErrors("create schema test");
        execute("" +
                "create alias test.someValues as $$ \n" +
                "import org.h2.tools.SimpleResultSet;\n" +
                "import java.sql.ResultSet;\n" +
                "import java.sql.Types;\n" +
                "@CODE" +
                "ResultSet someValuesInternal() { \n" +
                "    SimpleResultSet rs = new SimpleResultSet();\n" +
                "    rs.addColumn(\"ID\", Types.INTEGER, 10, 0);\n" +
                "    rs.addColumn(\"NAME\", Types.VARCHAR, 255, 0);\n" +
                "    rs.addRow(15, \"Hello\");\n" +
                "    rs.addRow(42, \"World\");\n" +
                "    return rs; \n" +
                "}" +
                "$$;"
        );
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
    public void withPreparedCallAndResultSet() throws SQLException {
        CallableStatement statement = connection.prepareCall("{? = call test.someValues() }");
        statement.execute();
        ResultSet resultSet = statement.getResultSet();
        resultSet.next();
        resultSet.next();

        assertThat(resultSet.getString(2), equalTo("World"));
    }
}
