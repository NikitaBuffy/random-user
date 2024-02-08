package ru.pominov.randomuser.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.pominov.randomuser.service.user.UserService;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

@Component
@Slf4j
@RequiredArgsConstructor
public class ApplicationRunner implements CommandLineRunner {

    private final RandomUserMeClient randomUserMeClient;
    private final UserService userService;

    @Override
    public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Какое действие вы хотите сделать?");
            System.out.println("1. Загрузить пользователей в БД");
            System.out.println("2. Выгрузить пользователей из БД в CSV");
            System.out.println("3. Выйти из приложения");
            System.out.print("Введите номер действия: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Некорректный ввод. Пожалуйста, введите номер действия.");
                continue;
            }

            switch (choice) {
                case 1:
                    loadToDatabaseOption();
                    break;
                case 2:
//                    exportToCSV();
                    break;
                case 3:
                    System.out.println("Выход из приложения.");
                    return;
                default:
                    System.out.println("Некорректный ввод. Пожалуйста, выберите одно из доступных действий.");
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
                System.out.println("Некорректный ввод. Попробуйте снова.");
            }
        }

        ResponseEntity<String> response = randomUserMeClient.getRandomUsers(params);
        userService.saveToDatabase(response.getBody());
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
}
