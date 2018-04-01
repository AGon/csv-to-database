package ru.goncharov.ctd.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.goncharov.ctd.service.ConnectionService;

import java.sql.Connection;
import java.sql.DriverManager;

@Slf4j
@Service
public class ConnectionServiceImpl implements ConnectionService {

    @Value("${driver-name}")
    private String driverName;
    @Value("${database-url}")
    private String databaseUrl;
    @Value("${username}")
    private String username;
    @Value("${password}")
    private String password;

    public ConnectionServiceImpl() {
    }

    @Override
    public Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName(driverName).newInstance();
            connection = DriverManager.getConnection(
                    databaseUrl,
                    username,
                    password
            );
        } catch (final Exception e) {
            log.error("Ошибка получения соединения. ", e);
        }
        return connection;
    }
}
