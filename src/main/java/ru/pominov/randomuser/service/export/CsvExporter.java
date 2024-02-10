package ru.pominov.randomuser.service.export;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Component;
import ru.pominov.randomuser.model.User;
import ru.pominov.randomuser.model.UserLocation;
import ru.pominov.randomuser.model.UserLogin;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.function.Function;

/**
 * Класс для экспорта списка пользователей в формат CSV.
 * Имплементирует интерфейс ExportStrategy с переопределением единственного метода export().
 */
@Component
@Slf4j
public class CsvExporter implements ExportStrategy {

    /* Вынес на уровне константы, но есть и другие опциональные решения:
     * 1. Вынесение в properties
     * 2. Позволить пользователю самому передать путь файла
     */
    private static final String CSV_FILE_PATH = "users.csv";

    @Override
    public void export(List<User> users) {
        // Подразумеваем, что потенциально будет много записей, поэтому создаем BufferedWriter
        try (CSVPrinter printer = new CSVPrinter(new BufferedWriter(new FileWriter(CSV_FILE_PATH)), CSVFormat.DEFAULT)) {
            printer.printRecord("ID", "Title", "FirstName", "LastName", "E-mail", "Phone", "Cell", "Gender",
                    "Age", "DateOfBirth", "Nationality", "Registered", "RegistrationAge", "LargePicture", "MediumPicture",
                    "ThumbnailPicture", "City", "StreetName", "StreetNumber", "State", "Country", "Postcode", "Latitude",
                    "Longitude", "TimezoneOffset", "TimezoneDescription", "UUID", "Username");

            for (User user : users) {
                UserLocation userLocation = user.getUserLocation();
                UserLogin userLogin = user.getUserLogin();
                printer.printRecord(
                        // Использование тернарных операторов и Optional выглядит избыточно для проверки на null,
                        // исключения NPE не будет, ячейка просто будет пустая (касается полей User)
                        user.getId(),
                        user.getTitle(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getEmail(),
                        user.getPhone(),
                        user.getCell(),
                        user.getGender(),
                        user.getAge(),
                        user.getDateOfBirth(),
                        user.getNationality(),
                        user.getRegistered(),
                        user.getRegistrationAge(),
                        user.getLargePicture(),
                        user.getMediumPicture(),
                        user.getThumbnailPicture(),
                        // Для вложенного объекта используем тернарный оператор
                        userLocation != null ? userLocation.getCity() : null,
                        userLocation != null ? userLocation.getStreetName() : null,
                        userLocation != null ? userLocation.getStreetNumber() : null,
                        userLocation != null ? userLocation.getState() : null,
                        userLocation != null ? userLocation.getCountry() : null,
                        userLocation != null ? userLocation.getPostcode() : null,
                        userLocation != null ? userLocation.getLat() : null,
                        userLocation != null ? userLocation.getLat() : null,
                        userLocation != null ? userLocation.getTimezoneOffset() : null,
                        userLocation != null ? userLocation.getTimezoneDescription() : null,
                        // Просто ради примера вместо обычного тернарного сделал свой общего назначения
                        getValueOrDefault(userLogin, UserLogin::getUuid),
                        getValueOrDefault(userLogin, UserLogin::getUsername)
                );
            }
            log.info("Successful export to CSV file '{}'", CSV_FILE_PATH);
            System.out.println("\nУспешная выгрузка. CSV файл создан в корневой директории проекта.");

        } catch (IOException e) {
            log.error("Error while exporting to CSV: " + e.getMessage());
            System.out.println("Произошла ошибка. Пожалуйста, попробуйте еще раз.");
        }
    }

    private <T, R> R getValueOrDefault(T obj, Function<T, R> getter) {
        return obj != null ? getter.apply(obj) : null;
    }

}
