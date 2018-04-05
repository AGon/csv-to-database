package ru.goncharov.ctd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import ru.goncharov.ctd.parser.ToDatabaseParser;
import ru.goncharov.ctd.parser.impl.CsvToDatabaseParser;

@SpringBootApplication
public class CsvToDatabaseApplication {

    public static void main(String[] args) {
        final ApplicationContext context = SpringApplication.run(CsvToDatabaseApplication.class, args);
        final ToDatabaseParser parser = context.getBean(CsvToDatabaseParser.class);
        parser.run();
    }
}
