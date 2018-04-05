package ru.goncharov.ctd.service.impl;

import com.google.common.base.Joiner;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.goncharov.ctd.service.DatabaseService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.lang.String.format;

@Service
public class DatabaseServiceImpl implements DatabaseService {

    @Value("${table}")
    private String table;
    @Value("${column-size}")
    private String columnSize;
    @Value("${column-name}")
    private String columnName;

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DatabaseServiceImpl(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void dropTableIfExist() {
        jdbcTemplate.execute(format("DROP TABLE IF EXISTS %s", table));
    }

    @Override
    public void createTable(final int columnsCount) {
        final StringBuilder query = new StringBuilder();
        query.append(format("CREATE TABLE %s(", table));
        for (int columnNumber = 1; columnNumber <= columnsCount; columnNumber++) {
            query.append(columnName).append(columnNumber).append(format(" VARCHAR(%s) NOT NULL, ", columnSize));
        }
        query.append(format("PRIMARY KEY(%s%s))", columnName, "1"));
        jdbcTemplate.execute(query.toString());
    }

    @Override
    public void insertIntoTable(List<CSVRecord> records, final int columnsCount) {
        final String query = format("INSERT INTO %s VALUES(%s)", table, generateParams(columnsCount));
        for (final CSVRecord record : records) {
            jdbcTemplate.update(query, getArgs(record, columnsCount));
        }
    }

    private String generateParams(final int columnsCount) {
        return Joiner.on(", ").join(Collections.nCopies(columnsCount, "?"));
    }

    private Object[] getArgs(final CSVRecord record, final int columnsCount) {
        final List<String> result = new ArrayList<>();
        for (int columnNumber = 0; columnNumber < columnsCount; columnNumber++) {
            result.add(record.get(columnNumber));
        }
        return result.toArray();
    }
}
