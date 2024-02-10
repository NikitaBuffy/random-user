package ru.pominov.randomuser.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.pominov.randomuser.service.user.UserService;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Класс для консольного взаимодействия с пользователем.
 * Имплементирует интерфейс CommandLineRunner для создания консольного приложения, а не веб.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class ApplicationRunner implements CommandLineRunner {

    private final UserService userService;

    // Код, запускающийся при старте приложения
    @Override
    public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nКакое действие вы хотите сделать?");
            System.out.println("1. Загрузить пользователей в БД");
            System.out.println("2. Выгрузить пользователей из БД");
            System.out.println("3. Выйти из приложения");
            System.out.print("Введите номер действия: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("\nНекорректный ввод. Пожалуйста, введите номер действия.");
                continue;
            }

            switch (choice) {
                case 1:
                    loadToDatabaseOption();
                    break;
                case 2:
                    exportOption();
                    break;
                case 3:
                    System.out.println("\nВыход из приложения.");
                    return;
                default:
                    System.out.println("\nНекорректный ввод. Пожалуйста, выберите одно из доступных действий.");
            }
        }
    }

    private void loadToDatabaseOption() {
        Scanner scanner = new Scanner(System.in);
        Map<String, String> params = new LinkedHashMap<>();
        System.out.println("\nВведите параметры запроса (название=значение), для завершения введите 'done':");
        System.out.println("Для просмотра доступных параметров запроса введите 'help'");
        System.out.println("Для возврата в предыдущее меню введите 'back'");

        String input;
        while (!(input = scanner.nextLine()).equalsIgnoreCase("done")) {
            if (input.equals("back")) {
                return; // Возвращаемся в предыдущее меню
            } else if (input.equalsIgnoreCase("help")) {
                displayAvailableValues(); // Отображаем таблицу доступных параметров
                continue; // Продолжаем цикл, чтобы снова запросить ввод
            }
            String[] parts = input.split("=");
            if (parts.length == 2) {
                params.put(parts[0].trim(), parts[1].trim());
            } else {
                System.out.println("\nНекорректный ввод. Попробуйте снова.");
            }
        }

        userService.saveToDatabase(params);
    }

    private void displayAvailableValues() {
        System.out.println("\nДоступные параметры запроса:");
        System.out.println("---------------------------------------------------------------------------------------------------");
        System.out.println("| Параметр | Значение                                                                             |");
        System.out.println("---------------------------------------------------------------------------------------------------");
        System.out.println("| results  | 2 - 5000                                                                             |");
        System.out.println("| gender   | male, female                                                                         |");
        System.out.println("| nat      | AU, BR, CA, CH, DE, DK, ES, FI, FR, GB, IE, IN, IR, MX, NL, NO, NZ, RS, TR, UA, US   |");
        System.out.println("| inc      | gender, name, location, email, login, registered, dob, phone, cell, id, picture, nat |");
        System.out.println("| exc      | gender, name, location, email, login, registered, dob, phone, cell, id, picture, nat |");
        System.out.println("---------------------------------------------------------------------------------------------------");
    }

    private void exportOption() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nВ каком формате необходима выгрузка?");
        System.out.println("1. CSV");
        System.out.println("2. Excel");
        System.out.println("3. Возврат в предыдущее меню");
        System.out.print("Введите номер действия: ");

        int choice;
        try {
            choice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("\nНекорректный ввод. Пожалуйста, введите номер действия.");
            exportOption();
            return;
        }

        switch (choice) {
            case 1:
                System.out.println("\nВведите количество пользователей для выгрузки в CSV или 'all' для выгрузки всех:");
                String countInput = scanner.nextLine();
                if (countInput.equalsIgnoreCase("all")) {
                    // Помечаем 0 как условие, что необходима выгрузка всех пользователей
                    userService.getFromDatabase("CSV", 0);
                } else {
                    try {
                        int count = Integer.parseInt(countInput);
                        userService.getFromDatabase("CSV", count);
                    } catch (NumberFormatException e) {
                        System.out.println("\nНекорректный ввод количества пользователей.");
                    }
                }
                break;
            case 2:
                System.out.println("\nВведите количество пользователей для выгрузки в Excel или 'all' для выгрузки всех:");
                String countInputExcel = scanner.nextLine();
                if (countInputExcel.equalsIgnoreCase("all")) {
                    // Помечаем 0 как условие, что необходима выгрузка всех пользователей
                    userService.getFromDatabase("Excel", 0);
                } else {
                    try {
                        int count = Integer.parseInt(countInputExcel);
                        userService.getFromDatabase("Excel", count);
                    } catch (NumberFormatException e) {
                        System.out.println("\nНекорректный ввод количества пользователей.");
                    }
                }
                break;
            case 3:
                return;
            default:
                System.out.println("\nНекорректный ввод. Пожалуйста, выберите одно из доступных действий.");
                exportOption();
        }
    }
}
