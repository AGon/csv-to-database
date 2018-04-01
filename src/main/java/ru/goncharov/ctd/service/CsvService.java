package ru.goncharov.ctd.service;

import org.apache.commons.csv.CSVRecord;

import java.util.List;

public interface CsvService {

    /**
     * Получить список записей из CSV файла.
     *
     * @return Список записей из CSV файла
     */
    List<CSVRecord> getRecords();
}
