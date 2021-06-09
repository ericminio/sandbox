package ericminio.sql.h2;

import org.junit.Before;

import java.sql.Connection;
import java.sql.SQLException;

import static java.sql.DriverManager.getConnection;

public class DatabaseTest {

    protected Connection connection;

    @Before
    public void connect() throws SQLException, ClassNotFoundException {
        Class.forName("org.h2.Driver");
        connection = getConnection("jdbc:h2:mem:myDb;DB_CLOSE_DELAY=-1", "sa", "sa");
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
