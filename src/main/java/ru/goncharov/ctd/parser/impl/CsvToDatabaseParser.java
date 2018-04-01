package ru.goncharov.ctd.parser.impl;

import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.dbutils.DbUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.goncharov.ctd.parser.ToDatabaseParser;
import ru.goncharov.ctd.service.ConnectionService;
import ru.goncharov.ctd.service.CsvService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;

import static java.lang.String.format;

@Slf4j
@Component
public class CsvToDatabaseParser implements ToDatabaseParser {

    @Value("${table}")
    private String table;
    @Value("${column-size}")
    private String columnSize;
    @Value("${column-name}")
    private String columnName;

    private final CsvService csvService;
    private final ConnectionService connectionService;

    @Autowired
    public CsvToDatabaseParser(final CsvService csvService, final ConnectionService connectionService) {
        this.csvService = csvService;
        this.connectionService = connectionService;
    }

    @Override
    public void run() {
        final List<CSVRecord> records = csvService.getRecords();
        if (records != null && !records.isEmpty()) {
            final int columnsCount = records.get(0).size(); // Считаем столбцы в первой строке
            Connection connection = null;
            try {
                connection = connectionService.getConnection();
                if (connection != null) {
                    recreateTable(connection, columnsCount);
                    for (final CSVRecord record : records) {
                        insertToTable(record, columnsCount, connection);
                    }
                    log.info("Выполнено.");
                } else {
                    log.info("Отсутствует соединение с БД.");
                }
            } finally {
                DbUtils.closeQuietly(connection);
            }
        } else {
            log.info("Отсутствуют доступные строки для записи в БД.");
        }
    }

    private void insertToTable(final CSVRecord record, final int columnsCount, final Connection connection) {
        final String insertQuery = format("INSERT INTO %s VALUES(%s);", table, generateParams(columnsCount));
        final PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement(insertQuery);
            for (int columnNumber = 0; columnNumber < columnsCount; columnNumber++) {
                preparedStatement.setString((columnNumber + 1), record.get(columnNumber));
            }
            preparedStatement.execute();
        } catch (SQLException e) {
            log.error("Ошибка при работе с БД.", e);
        }
    }

    private String generateParams(final int columnsCount) {
        return Joiner.on(", ").join(Collections.nCopies(columnsCount, "?"));
    }

    private void recreateTable(final Connection connection, final int columnsCount) {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            dropTableIfExist(statement);
            createTable(columnsCount, statement);
        } catch (SQLException e) {
            log.error("Ошибка при работе с БД.", e);
        } finally {
            DbUtils.closeQuietly(statement);
        }
    }

    private void dropTableIfExist(final Statement statement) throws SQLException {
        final String query = format("DROP TABLE IF EXISTS %s;", table);
        statement.execute(query);
    }

    private void createTable(final int columnsCount, final Statement statement) throws SQLException {
        final StringBuilder query = new StringBuilder();
        query.append(format("CREATE TABLE %s(", table));
        for (int columnNumber = 1; columnNumber <= columnsCount; columnNumber++) {
            query.append(columnName).append(columnNumber).append(format(" VARCHAR(%s) NOT NULL, ", columnSize));
        }
        query.append(format("PRIMARY KEY(%s%s));", columnName, "1"));
        statement.execute(query.toString());
    }
}
