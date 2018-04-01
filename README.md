# csv-to-database
Сохранить CSV файл в таблице базы данных.

##### Параметры запуска:

- Общие настройки
  - ```--path-to-csv=``` : путь к CSV файлу;
  - ```--charset=``` : кодировка файла (по умолчанию: ```UTF-8```);
  - ```--table=``` : название создаваемой таблицы в базе данных (по умолчанию: ```TableFromCsv```);
  - ```--column-size=``` : размер строки в столбце (по умолчанию: ```512```);
  - ```--column-name=``` : название столбца (по умолчанию: ```COLUMN```);
  - ```--skip-header=``` : пропустить первую строку (по умолчанию: ```false```).

- Настройки подключения к БД
  - ```--driver-name=``` : название JDBC-драйвера;
  - ```--database-url=``` : URL для подключения к базе данных;
  - ```--username=``` : имя пользователя;
  - ```--password=``` : пароль.
  
##### Пример запуска:
  
  ```
  java -cp csv-to-database-0.1.jar -Dloader.path=./lib/* \
    org.springframework.boot.loader.PropertiesLauncher \
        --path-to-csv="data-20171211T1403-structure-20171211T1403.csv" \
        --skip-header="true" \
        --driver-name="org.h2.Driver" \
        --database-url="jdbc:h2:~/test" \
        --username="sa"
  ```
  
##### Примечание
В каталог ```lib``` необходимо скопировать JDBC-драйвер для соответствующей БД.
