package ru.pominov.randomuser.service.export;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Component;
import ru.pominov.randomuser.model.User;
import ru.pominov.randomuser.model.UserLocation;
import ru.pominov.randomuser.model.UserPicture;
import ru.pominov.randomuser.model.UserLogin;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class CsvExporter implements ExportStrategy {

    @Override
    public void export(List<User> users) {
        String csvFilePath = "users.csv";

        try (CSVPrinter printer = new CSVPrinter(new FileWriter(csvFilePath), CSVFormat.DEFAULT)) {
            printer.printRecord("ID", "Title", "FirstName", "LastName", "E-mail", "Phone", "Cell", "Gender",
                    "Age", "DateOfBirth", "Nationality", "Registered", "RegistrationAge", "LargePicture", "MediumPicture",
                    "ThumbnailPicture", "City", "StreetName", "StreetNumber", "State", "Country", "Postcode", "Latitude",
                    "Longitude", "TimezoneOffset", "TimezoneDescription", "UUID", "Username");

            for (User user : users) {
                printer.printRecord(
                        user.getId(),
                        // Используем Optional, так как поля в объекте могут отсутствовать после загрузки с параметрами API RandomUserMe
                        Optional.ofNullable(user.getTitle()).orElse(null),
                        Optional.ofNullable(user.getFirstName()).orElse(null),
                        Optional.ofNullable(user.getLastName()).orElse(null),
                        Optional.ofNullable(user.getEmail()).orElse(null),
                        Optional.ofNullable(user.getPhone()).orElse(null),
                        Optional.ofNullable(user.getCell()).orElse(null),
                        Optional.ofNullable(user.getGender()).orElse(null),
                        Optional.ofNullable(user.getAge()).orElse(null),
                        Optional.ofNullable(user.getDateOfBirth()).orElse(null),
                        Optional.ofNullable(user.getNationality()).orElse(null),
                        Optional.ofNullable(user.getRegistered()).orElse(null),
                        Optional.ofNullable(user.getRegistrationAge()).orElse(null),
                        Optional.ofNullable(user.getUserPicture()).map(UserPicture::getLarge).orElse(null),
                        Optional.ofNullable(user.getUserPicture()).map(UserPicture::getMedium).orElse(null),
                        Optional.ofNullable(user.getUserPicture()).map(UserPicture::getThumbnail).orElse(null),
                        Optional.ofNullable(user.getUserLocation()).map(UserLocation::getCity).orElse(null),
                        Optional.ofNullable(user.getUserLocation()).map(UserLocation::getStreetName).orElse(null),
                        Optional.ofNullable(user.getUserLocation()).map(UserLocation::getStreetNumber).orElse(null),
                        Optional.ofNullable(user.getUserLocation()).map(UserLocation::getState).orElse(null),
                        Optional.ofNullable(user.getUserLocation()).map(UserLocation::getCountry).orElse(null),
                        Optional.ofNullable(user.getUserLocation()).map(UserLocation::getPostcode).orElse(null),
                        Optional.ofNullable(user.getUserLocation()).map(UserLocation::getLat).orElse(null),
                        Optional.ofNullable(user.getUserLocation()).map(UserLocation::getLon).orElse(null),
                        Optional.ofNullable(user.getUserLocation()).map(UserLocation::getTimezoneOffset).orElse(null),
                        Optional.ofNullable(user.getUserLocation()).map(UserLocation::getTimezoneDescription).orElse(null),
                        Optional.ofNullable(user.getUserLogin()).map(UserLogin::getUuid).orElse(null),
                        Optional.ofNullable(user.getUserLogin()).map(UserLogin::getUsername).orElse(null)
                );
            }
            log.info("Successful export to CSV file '{}'", csvFilePath);
            System.out.println("\nУспешная выгрузка. CSV файл создан в корневой директории проекта.");

        } catch (IOException e) {
            log.error("Error while exporting to CSV: " + e.getMessage());
            System.out.println("Произошла ошибка. Пожалуйста, попробуйте еще раз.");
        }
    }
}
