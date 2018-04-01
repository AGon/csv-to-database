package ru.goncharov.ctd.service;

import java.sql.Connection;

public interface ConnectionService {

    /**
     * Получить новый сеанс работы с БД.
     *
     * @return Новый сеанс работы с БД
     */
    Connection getConnection();
}
