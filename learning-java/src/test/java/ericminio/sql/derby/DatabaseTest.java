package ericminio.sql.derby;

import org.junit.Before;

import java.sql.Connection;
import java.sql.SQLException;

import static java.sql.DriverManager.getConnection;

public class DatabaseTest {

    protected Connection connection;

    @Before
    public void connect() throws ClassNotFoundException, SQLException {
        connection = getConnection("jdbc:derby:memory:myDb;create=true", "sa", "sa");
    }

    protected void execute(String sql) throws SQLException {
        connection.prepareCall(sql).execute();
    }

    protected void executeIgnoringErrors(String sql) throws SQLException {
        try {
            connection.prepareCall(sql).execute();
        }
        catch (Exception ignored) {}
    }
}
