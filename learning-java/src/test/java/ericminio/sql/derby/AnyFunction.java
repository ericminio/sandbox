package ericminio.sql.derby;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static java.sql.DriverManager.getConnection;

public class AnyFunction {

    public static ResultSet read() throws SQLException {
        Connection connection = getConnection("jdbc:derby:memory:myDb;create=true", "sa", "sa");
        PreparedStatement statement = connection.prepareStatement("select name from product");
        return statement.executeQuery();
    }
}
