package ru.goncharov.ctd.parser.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.goncharov.ctd.parser.ToDatabaseParser;
import ru.goncharov.ctd.service.CsvService;
import ru.goncharov.ctd.service.DatabaseService;

import java.util.List;

@Slf4j
@Component
public class CsvToDatabaseParser implements ToDatabaseParser {

    private final CsvService csvService;
    private final DatabaseService databaseService;

    @Autowired
    public CsvToDatabaseParser(final CsvService csvService, final DatabaseService databaseService) {
        this.csvService = csvService;
        this.databaseService = databaseService;
    }

    @Override
    public void run() {
        final List<CSVRecord> records = csvService.getRecords();
        if (records != null && !records.isEmpty()) {
            final int columnsCount = records.get(0).size(); // Считаем столбцы в первой строке
            databaseService.dropTableIfExist();
            databaseService.createTable(columnsCount);
            databaseService.insertIntoTable(records, columnsCount);
            log.info("Выполнено.");
        } else {
            log.info("Отсутствуют доступные строки для записи в БД.");
        }
    }
}
