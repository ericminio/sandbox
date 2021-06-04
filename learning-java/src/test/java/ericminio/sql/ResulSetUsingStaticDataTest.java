package ericminio.sql;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ResulSetUsingStaticDataTest {

    ResulSetUsingStaticData resulSetUsingStaticData;

    @Before
    public void sut() {
        resulSetUsingStaticData = new ResulSetUsingStaticData();
    }

    @Test
    public void emptyByDefault() throws SQLException {
        ResultSet resultSet = resulSetUsingStaticData;

        assertThat(resultSet.next(), equalTo(false));
    }

    @Test
    public void knowsWhenToStop() throws SQLException {
        resulSetUsingStaticData.setData(Arrays.asList(Arrays.asList("hello world")));
        ResultSet resultSet = resulSetUsingStaticData;

        assertThat(resultSet.next(), equalTo(true));
        assertThat(resultSet.next(), equalTo(false));
    }

    @Test
    public void canReturnString() throws SQLException {
        resulSetUsingStaticData.setData(Arrays.asList(Arrays.asList("hello world")));
        ResultSet resultSet = resulSetUsingStaticData;
        resultSet.next();

        assertThat(resultSet.getString(1), equalTo("hello world"));
    }

    @Test
    public void canReturnSeveralRecords() throws SQLException {
        resulSetUsingStaticData.setData(Arrays.asList(
                Arrays.asList("h", "e", "l", "l", "o"),
                Arrays.asList("w", "o", "r", "l", "d")
        ));
        ResultSet resultSet = resulSetUsingStaticData;
        resultSet.next();
        resultSet.next();

        assertThat(resultSet.getString(2), equalTo("o"));
    }

    @Test
    public void canReturnBigDecimal() throws SQLException {
        resulSetUsingStaticData.setData(Arrays.asList(Arrays.asList(new BigDecimal(15))));
        ResultSet resultSet = resulSetUsingStaticData;
        resultSet.next();

        assertThat(resultSet.getBigDecimal(1), equalTo(new BigDecimal(15)));
    }

    @Test
    public void canReturnTimestamp() throws SQLException, ParseException {
        Timestamp expected = new Timestamp(
                new SimpleDateFormat("yyyy/M/dd hh:mm:ss")
                .parse("2015/01/01 19:15:00").getTime());
        resulSetUsingStaticData.setData(Arrays.asList(Arrays.asList(expected)));
        ResultSet resultSet = resulSetUsingStaticData;
        resultSet.next();

        assertThat(resultSet.getTimestamp(1), equalTo(expected));
    }
}
