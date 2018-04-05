package ru.goncharov.ctd.service;

import org.apache.commons.csv.CSVRecord;

import java.util.List;

public interface DatabaseService {

    /**
     * Проверить существование и удалить таблицу.
     */
    void dropTableIfExist();

    /**
     * Создать таблицу.
     *
     * @param columnsCount Количество столбцов
     */
    void createTable(final int columnsCount);

    /**
     * Вставить запись из CSV-файла в таблицу базы данных.
     *
     * @param records      Запись из CSV-файла
     * @param columnsCount Количество столбцов
     */
    void insertIntoTable(final List<CSVRecord> records, final int columnsCount);
}
