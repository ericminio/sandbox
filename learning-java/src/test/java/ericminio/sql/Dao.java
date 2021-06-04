package ericminio.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Dao {

    private Repository repository;

    public List<String> findAll() throws SQLException {
        List<String> values = new ArrayList<>();
        ResultSet resultSet = repository.fetchAll();
        while (resultSet.next()) {
            values.add(resultSet.getString(1));
        }
        return values;
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }
}
