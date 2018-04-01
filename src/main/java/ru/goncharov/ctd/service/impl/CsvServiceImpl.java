package ru.goncharov.ctd.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.goncharov.ctd.service.CsvService;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;

import static java.lang.String.format;

@Slf4j
@Service
public class CsvServiceImpl implements CsvService {

    @Value("${path-to-csv}")
    private String pathToCsv;
    @Value("${charset}")
    private String charset;
    @Value("${skip-header}")
    private boolean skipHeader;

    @Override
    public List<CSVRecord> getRecords() {
        List<CSVRecord> records = null;
        try {
            records = createCsvParser().getRecords();
        } catch (IOException e) {
            log.error(format("Ошибка при работе с файлом '%s'.", pathToCsv), e);
        }
        return records;
    }

    private CSVParser createCsvParser() throws IOException {
        return createCsvFormat()
                .parse(new BufferedReader(
                                new InputStreamReader(
                                        new FileInputStream(pathToCsv),
                                        Charset.forName(charset)
                                )
                        )
                );
    }

    private CSVFormat createCsvFormat() {
        return skipHeader ?
                CSVFormat.DEFAULT
                        .withHeader()
                        .withSkipHeaderRecord()
                :
                CSVFormat.DEFAULT;
    }
}
