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
import ru.pominov.randomuser.model.User;
import ru.pominov.randomuser.model.UserLocation;
import ru.pominov.randomuser.model.UserLogin;

import java.io.*;
import java.time.LocalDateTime;

import java.util.List;
import java.util.UUID;

/**
 * Класс для экспорта списка пользователей в формат Excel.
 * Имплементирует интерфейс ExportStrategy с переопределением единственного метода export().
 */
@Component
@Slf4j
public class ExcelExporter implements ExportStrategy {
    /* Вынес на уровне константы, но есть и другие опциональные решения:
     * 1. Вынесение в properties
     * 2. Позволить пользователю самому передать путь файла
     */
    private static final String EXCEL_FILE_PATH = "users.xlsx";
    private XSSFSheet sheet;

    @Override
    public void export(List<User> users) {
        // Подразумеваем, что потенциально будет много записей, поэтому создаем BufferedOutputStream
        try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(EXCEL_FILE_PATH));
            XSSFWorkbook workbook = new XSSFWorkbook()) {
            createHeader(workbook);
            write(users, workbook);
            workbook.write(outputStream);
            workbook.close();

            log.info("Successful export to Excel file '{}'", EXCEL_FILE_PATH);
            System.out.println("\nУспешная выгрузка. Excel файл создан в корневой директории проекта.");
        } catch (IOException e) {
            log.error("Error while exporting to Excel: " + e.getMessage());
            System.out.println("Произошла ошибка. Пожалуйста, попробуйте еще раз.");
        }
    }

    private void createHeader(XSSFWorkbook workbook) {
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
            createCell(row, i, headers[i], style, workbook);
        }
    }

    private void write(List<User> users, XSSFWorkbook workbook) {
        int rowCount = 1;
        //Устанавливаем обычный размер и толщину шрифта
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);

        for (User user : users) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            // Использование тернарных операторов и Optional выглядит избыточно, так как не будет NPE,
            // в таблице будет пустая ячейка
            createCell(row, columnCount++, user.getId(), style, workbook);
            createCell(row, columnCount++, user.getTitle(), style, workbook);
            createCell(row, columnCount++, user.getFirstName(), style, workbook);
            createCell(row, columnCount++, user.getLastName(), style, workbook);
            createCell(row, columnCount++, user.getEmail(), style, workbook);
            createCell(row, columnCount++, user.getPhone(), style, workbook);
            createCell(row, columnCount++, user.getCell(), style, workbook);
            createCell(row, columnCount++, user.getGender(), style, workbook);
            createCell(row, columnCount++, user.getAge(), style, workbook);
            createCell(row, columnCount++, user.getDateOfBirth(), style, workbook);
            createCell(row, columnCount++, user.getNationality(), style, workbook);
            createCell(row, columnCount++, user.getRegistered(), style, workbook);
            createCell(row, columnCount++, user.getRegistrationAge(), style, workbook);
            createCell(row, columnCount++, user.getLargePicture(), style, workbook);
            createCell(row, columnCount++, user.getMediumPicture(), style, workbook);
            createCell(row, columnCount++, user.getThumbnailPicture(), style, workbook);

            // Избавляет от использования Optional с map и лямбдами
            UserLocation userLocation = user.getUserLocation();
            if (userLocation != null) {
                createCell(row, columnCount++, userLocation.getCity(), style, workbook);
                createCell(row, columnCount++, userLocation.getStreetName(), style, workbook);
                createCell(row, columnCount++, userLocation.getStreetNumber(), style, workbook);
                createCell(row, columnCount++, userLocation.getState(), style, workbook);
                createCell(row, columnCount++, userLocation.getCountry(), style, workbook);
                createCell(row, columnCount++, userLocation.getPostcode(), style, workbook);
                createCell(row, columnCount++, userLocation.getLat(), style, workbook);
                createCell(row, columnCount++, userLocation.getLon(), style, workbook);
                createCell(row, columnCount++, userLocation.getTimezoneOffset(), style, workbook);
                createCell(row, columnCount++, userLocation.getTimezoneDescription(), style, workbook);
            } else {
                //если userLocation == null, то мы просто пропускаем эти ячейки.
                columnCount += 10;
            }

            UserLogin userLogin = user.getUserLogin();
            if (userLogin != null) {
                createCell(row, columnCount++, userLogin.getUuid(), style, workbook);
                createCell(row, columnCount++, userLogin.getUsername(), style, workbook);
            } else {
                columnCount += 2;
            }
        }
    }

    private void createCell(Row row, int column, Object value, CellStyle style, XSSFWorkbook workbook) {
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
