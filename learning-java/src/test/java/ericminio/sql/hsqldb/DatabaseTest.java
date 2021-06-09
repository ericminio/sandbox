package ericminio.sql.hsqldb;

import org.junit.Before;

import java.sql.Connection;
import java.sql.SQLException;

import static java.sql.DriverManager.getConnection;

public class DatabaseTest {

    protected Connection connection;

    @Before
    public void connect() throws SQLException, ClassNotFoundException {
        Class.forName("org.hsqldb.jdbcDriver");
        connection = getConnection("jdbc:hsqldb:mem:mymemdb", "sa", "sa");
    }

    protected void execute(String sql) throws SQLException {
        connection.prepareCall(sql).execute();
    }

    protected void executeIgnoringErrors(String sql) {
        try {
            connection.prepareCall(sql).execute();
        }
        catch (Exception ignored) {}
    }
}
