package ericminio.sql;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ResultSetUsingStaticDataTest {

    ResultSetUsingStaticData resultSetUsingStaticData;

    @Before
    public void sut() {
        resultSetUsingStaticData = new ResultSetUsingStaticData();
    }

    @Test
    public void emptyByDefault() throws SQLException {
        ResultSet resultSet = resultSetUsingStaticData;

        assertThat(resultSet.next(), equalTo(false));
    }

    @Test
    public void knowsWhenToStop() throws SQLException {
        resultSetUsingStaticData.setData(Arrays.asList(Arrays.asList("hello world")));
        ResultSet resultSet = resultSetUsingStaticData;

        assertThat(resultSet.next(), equalTo(true));
        assertThat(resultSet.next(), equalTo(false));
    }

    @Test
    public void canReturnString() throws SQLException {
        resultSetUsingStaticData.setData(Arrays.asList(Arrays.asList("hello world")));
        ResultSet resultSet = resultSetUsingStaticData;
        resultSet.next();

        assertThat(resultSet.getString(1), equalTo("hello world"));
    }

    @Test
    public void canReturnSeveralRecords() throws SQLException {
        resultSetUsingStaticData.setData(Arrays.asList(
                Arrays.asList("h", "e", "l", "l", "o"),
                Arrays.asList("w", "o", "r", "l", "d")
        ));
        ResultSet resultSet = resultSetUsingStaticData;
        resultSet.next();
        resultSet.next();

        assertThat(resultSet.getString(2), equalTo("o"));
    }

    @Test
    public void canReturnBigDecimal() throws SQLException {
        resultSetUsingStaticData.setData(Arrays.asList(Arrays.asList(new BigDecimal(15))));
        ResultSet resultSet = resultSetUsingStaticData;
        resultSet.next();

        assertThat(resultSet.getBigDecimal(1), equalTo(new BigDecimal(15)));
    }

    @Test
    public void canReturnTimestamp() throws SQLException, ParseException {
        Timestamp expected = new Timestamp(new SimpleDateFormat("yyyy/M/dd hh:mm:ss")
                .parse("2015/01/01 19:15:00").getTime());
        resultSetUsingStaticData.setData(Arrays.asList(Arrays.asList(expected)));
        ResultSet resultSet = resultSetUsingStaticData;
        resultSet.next();

        assertThat(resultSet.getTimestamp(1), equalTo(expected));
    }

    @Test
    public void canReturnDate() throws SQLException, ParseException {
        Date expected = new SimpleDateFormat("yyyy/M/dd hh:mm:ss")
                .parse("2015/01/01 19:15:00");
        Timestamp input = new Timestamp(expected.getTime());
        resultSetUsingStaticData.setData(Arrays.asList(Arrays.asList(input)));
        ResultSet resultSet = resultSetUsingStaticData;
        resultSet.next();

        assertThat(resultSet.getDate(1), equalTo(expected));
    }

    @Test
    public void acceptsColumnNamesSetting() throws SQLException {
        resultSetUsingStaticData.setColumnNames(Arrays.asList("id", "name"));
        ResultSet resultSet = resultSetUsingStaticData;

        assertThat(resultSet.findColumn("id"), equalTo(1));
        assertThat(resultSet.findColumn("name"), equalTo(2));
    }

    @Test
    public void columnTypeIsGuessedFromDataOfFirstRecord() throws SQLException {
        resultSetUsingStaticData.setData(Arrays.asList(Arrays.asList("hello world")));
        resultSetUsingStaticData.setColumnNames(Arrays.asList("name"));
        ResultSet resultSet = resultSetUsingStaticData;

        assertThat(resultSet.getMetaData().getColumnType(resultSet.findColumn("name")), equalTo(Types.VARCHAR));
    }

    @Test
    public void columnTypeAlsoWorksForNumber() throws SQLException {
        resultSetUsingStaticData.setData(Arrays.asList(Arrays.asList(new BigDecimal(42))));
        resultSetUsingStaticData.setColumnNames(Arrays.asList("id"));
        ResultSet resultSet = resultSetUsingStaticData;

        assertThat(resultSet.getMetaData().getColumnType(resultSet.findColumn("id")), equalTo(Types.NUMERIC));
    }

    @Test
    public void columnTypeAlsoWorksForTimestamp() throws SQLException, ParseException {
        Timestamp value = new Timestamp(new SimpleDateFormat("yyyy/M/dd hh:mm:ss")
                        .parse("2015/01/01 19:15:00").getTime());
        resultSetUsingStaticData.setData(Arrays.asList(Arrays.asList(new BigDecimal(42), value)));
        resultSetUsingStaticData.setColumnNames(Arrays.asList("id", "creation"));
        ResultSet resultSet = resultSetUsingStaticData;

        assertThat(resultSet.getMetaData().getColumnType(resultSet.findColumn("creation")), equalTo(Types.TIMESTAMP));
    }

    @Test
    public void canReturnStringByColumnName() throws SQLException {
        resultSetUsingStaticData.setColumnNames(Arrays.asList("label"));
        resultSetUsingStaticData.setData(Arrays.asList(Arrays.asList("hello world")));
        ResultSet resultSet = resultSetUsingStaticData;
        resultSet.next();

        assertThat(resultSet.getString("label"), equalTo("hello world"));
    }

    @Test
    public void canReturnBigDecimalByColumnName() throws SQLException {
        resultSetUsingStaticData.setColumnNames(Arrays.asList("id"));
        resultSetUsingStaticData.setData(Arrays.asList(Arrays.asList(new BigDecimal(15))));
        ResultSet resultSet = resultSetUsingStaticData;
        resultSet.next();

        assertThat(resultSet.getBigDecimal("id"), equalTo(new BigDecimal(15)));
    }

    @Test
    public void canReturnTimestampByColumnName() throws SQLException, ParseException {
        resultSetUsingStaticData.setColumnNames(Arrays.asList("date"));
        Timestamp expected = new Timestamp(new SimpleDateFormat("yyyy/M/dd hh:mm:ss")
                .parse("2015/01/01 19:15:00").getTime());
        resultSetUsingStaticData.setData(Arrays.asList(Arrays.asList(expected)));
        ResultSet resultSet = resultSetUsingStaticData;
        resultSet.next();

        assertThat(resultSet.getTimestamp("date"), equalTo(expected));
    }

    @Test
    public void canReturnDateByColumnName() throws SQLException, ParseException {
        resultSetUsingStaticData.setColumnNames(Arrays.asList("date"));
        Date expected = new SimpleDateFormat("yyyy/M/dd hh:mm:ss")
                .parse("2015/01/01 19:15:00");
        Timestamp input = new Timestamp(expected.getTime());
        resultSetUsingStaticData.setData(Arrays.asList(Arrays.asList(input)));
        ResultSet resultSet = resultSetUsingStaticData;
        resultSet.next();

        assertThat(resultSet.getDate("date"), equalTo(expected));
    }
}
