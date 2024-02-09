package ru.pominov.randomuser.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.pominov.randomuser.model.user.User;
import ru.pominov.randomuser.repository.UserRepository;
import ru.pominov.randomuser.service.export.ExportStrategy;
import ru.pominov.randomuser.service.export.ExportStrategyFactory;
import ru.pominov.randomuser.util.JsonDeserializer;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final JsonDeserializer jsonDeserializer;
    private final UserRepository userRepository;
    private final ExportStrategyFactory exportStrategyFactory;

    @Override
    @Transactional
    public void saveToDatabase(String userJsonData) {
        System.out.println(userJsonData);
        // Десериализация данных из JSON в список пользователей
        List<User> users = jsonDeserializer.deserialize(userJsonData);

        List<User> savedUsers = userRepository.saveAll(users);
        log.info("Saved users to Database: {}", savedUsers);
    }

    @Override
    public void getFromDatabase(String exportMethod, int numberOfUsers) {
        List<User> users;
        try {
            // Выбор метода экспорта с применением паттерна проектирования 'Стратегия' с помощью ExportStrategyFactory
            ExportStrategy exportStrategy = exportStrategyFactory.createExportStrategy(exportMethod);
            if (numberOfUsers == 0) {
                // Выгрузка всех пользователей из БД
                users = userRepository.findAllOptimized();
            }
            else {
                Pageable pageable = PageRequest.of(0, numberOfUsers);
                // TODO: решить проблему N + 1 запроса в Hibernate
                users = userRepository.findAll(pageable).getContent();
            }

            if (users.isEmpty()) {
                log.debug("Cannot export users because database is empty.");
                System.out.println("\nВ БД отсутствуют пользователи, выгрузка недоступна.\n");
                return;
            }

            // Экспорт пользователей на основе выбранного метода
            exportStrategy.export(users);
        } catch (IllegalArgumentException e) {
            System.out.println("\nДанный метод не поддерживается.\n");
        }
    }
}
