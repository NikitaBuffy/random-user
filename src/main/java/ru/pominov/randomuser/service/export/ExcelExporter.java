package ru.pominov.randomuser.service.export;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import ru.pominov.randomuser.model.user.User;
import ru.pominov.randomuser.model.user.UserLocation;
import ru.pominov.randomuser.model.user.UserLogin;
import ru.pominov.randomuser.model.user.UserPicture;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@Slf4j
public class ExcelExporter implements ExportStrategy {

    private final XSSFWorkbook workbook;
    private XSSFSheet sheet;

    public ExcelExporter() {
        workbook = new XSSFWorkbook();
    }

    @Override
    public void export(List<User> users) {
        String excelFilePath = "users.xlsx";
        try (FileOutputStream outputStream = new FileOutputStream(excelFilePath)) {
            createHeader();
            write(users);
            workbook.write(outputStream);
            workbook.close();

            log.info("Successful export to Excel file '{}'", excelFilePath);
            System.out.println("\nУспешная выгрузка. Excel файл создан в корневой директории проекта.");
        } catch (IOException e) {
            log.error("Error while exporting to Excel: " + e.getMessage());
            System.out.println("Произошла ошибка. Пожалуйста, попробуйте еще раз.");
        }
    }

    private void createHeader() {
        // Создаем лист Users
        sheet = workbook.createSheet("Users");
        // Номер строки для записи данных
        Row row = sheet.createRow(0);
        // Устанавливаем размер и толщину шрифта
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);

        // Заголовки столбцов
        String[] headers = {"ID", "Title", "FirstName", "LastName", "E-mail", "Phone", "Cell", "Gender",
                "Age", "DateOfBirth", "Nationality", "Registered", "RegistrationAge", "LargePicture", "MediumPicture",
                "ThumbnailPicture", "City", "StreetName", "StreetNumber", "State", "Country", "Postcode", "Latitude",
                "Longitude", "TimezoneOffset", "TimezoneDescription", "UUID", "Username"};
        for (int i = 0; i < headers.length; i++) {
            createCell(row, i, headers[i], style);
        }
    }

    private void write(List<User> users) {
        int rowCount = 1;
        //Устанавливаем обычный размер и толщину шрифта
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);

        for (User user : users) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            createCell(row, columnCount++, user.getId(), style);
            createCell(row, columnCount++, Optional.ofNullable(user.getTitle()).orElse(null), style);
            createCell(row, columnCount++, Optional.ofNullable(user.getFirstName()).orElse(null), style);
            createCell(row, columnCount++, Optional.ofNullable(user.getLastName()).orElse(null), style);
            createCell(row, columnCount++, Optional.ofNullable(user.getEmail()).orElse(null), style);
            createCell(row, columnCount++, Optional.ofNullable(user.getPhone()).orElse(null), style);
            createCell(row, columnCount++, Optional.ofNullable(user.getCell()).orElse(null), style);
            createCell(row, columnCount++, Optional.ofNullable(user.getGender()).orElse(null), style);
            createCell(row, columnCount++, Optional.ofNullable(user.getAge()).orElse(null), style);
            createCell(row, columnCount++, Optional.ofNullable(user.getDateOfBirth()).orElse(null), style);
            createCell(row, columnCount++, Optional.ofNullable(user.getNationality()).orElse(null), style);
            createCell(row, columnCount++, Optional.ofNullable(user.getRegistered()).orElse(null), style);
            createCell(row, columnCount++, Optional.ofNullable(user.getRegistrationAge()).orElse(null), style);
            createCell(row, columnCount++, Optional.ofNullable(user.getUserPicture()).map(UserPicture::getLarge).orElse(null), style);
            createCell(row, columnCount++, Optional.ofNullable(user.getUserPicture()).map(UserPicture::getMedium).orElse(null), style);
            createCell(row, columnCount++, Optional.ofNullable(user.getUserPicture()).map(UserPicture::getThumbnail).orElse(null), style);
            createCell(row, columnCount++, Optional.ofNullable(user.getUserLocation()).map(UserLocation::getCity).orElse(null), style);
            createCell(row, columnCount++, Optional.ofNullable(user.getUserLocation()).map(UserLocation::getStreetName).orElse(null), style);
            createCell(row, columnCount++, Optional.ofNullable(user.getUserLocation()).map(UserLocation::getStreetNumber).orElse(null), style);
            createCell(row, columnCount++, Optional.ofNullable(user.getUserLocation()).map(UserLocation::getState).orElse(null), style);
            createCell(row, columnCount++, Optional.ofNullable(user.getUserLocation()).map(UserLocation::getCountry).orElse(null), style);
            createCell(row, columnCount++, Optional.ofNullable(user.getUserLocation()).map(UserLocation::getPostcode).orElse(null), style);
            createCell(row, columnCount++, Optional.ofNullable(user.getUserLocation()).map(UserLocation::getLat).orElse(null), style);
            createCell(row, columnCount++, Optional.ofNullable(user.getUserLocation()).map(UserLocation::getLon).orElse(null), style);
            createCell(row, columnCount++, Optional.ofNullable(user.getUserLocation()).map(UserLocation::getTimezoneOffset).orElse(null), style);
            createCell(row, columnCount++, Optional.ofNullable(user.getUserLocation()).map(UserLocation::getTimezoneDescription).orElse(null), style);
            createCell(row, columnCount++, Optional.ofNullable(user.getUserLogin()).map(UserLogin::getUuid).orElse(null), style);
            createCell(row, columnCount++, Optional.ofNullable(user.getUserLogin()).map(UserLogin::getUsername).orElse(null), style);
        }
    }

    private void createCell(Row row, int column, Object value, CellStyle style) {
        sheet.autoSizeColumn(column);
        Cell cell = row.createCell(column);

        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Long) {
            cell.setCellValue((Long) value);
        } else if (value instanceof Float) {
            cell.setCellValue((Float) value);
        } else if (value instanceof UUID) {
            cell.setCellValue(String.valueOf(value));
        } else if (value instanceof LocalDateTime) {
            cell.setCellValue((LocalDateTime) value);
            // Для даты/времени требуется дополнительная обработка форматирования
            CreationHelper creationHelper = workbook.getCreationHelper();
            // Создаем новый стиль
            CellStyle dateStyle = workbook.createCellStyle();
            // Клонируем предыдущий стиль с размером и толщиной шрифта
            dateStyle.cloneStyleFrom(style);
            // Задаем формат даты
            dateStyle.setDataFormat(creationHelper.createDataFormat().getFormat("yyyy-mm-dd hh:mm:ss"));
            cell.setCellStyle(dateStyle);
            // Прерываем метод, чтобы не применился обратно общий стиль в конце метода
            return;
        } else {
            cell.setCellValue((String) value);
        }

        cell.setCellStyle(style);
    }
}
