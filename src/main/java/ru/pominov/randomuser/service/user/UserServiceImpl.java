package ru.pominov.randomuser.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.client.HttpStatusCodeException;
import ru.pominov.randomuser.model.User;
import ru.pominov.randomuser.repository.UserRepository;
import ru.pominov.randomuser.service.RandomUserMeClient;
import ru.pominov.randomuser.service.export.ExportStrategy;
import ru.pominov.randomuser.service.export.ExportStrategyFactory;
import ru.pominov.randomuser.util.JsonDeserializer;

import java.util.List;
import java.util.Map;

/**
 * Основной класс приложения, являющийся имплементацией интерфейса UserService.
 * Осуществляет загрузку и выгрузку пользователей из БД.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final JsonDeserializer jsonDeserializer;
    private final UserRepository userRepository;
    private final ExportStrategyFactory exportStrategyFactory;
    private final RandomUserMeClient randomUserMeClient;

    /**
     * Метод отправляет запрос на {@link RandomUserMeClient#getRandomUsers(Map) получение пользователей},
     * получает ответ и JSON (в случае успеха), отправляет последний {@link JsonDeserializer#deserialize(String) на десериализацию}
     * и сохраняет в БД полученные объекты (один или несколько).
     * В случае, если ответ сервера 4xx или 5xx, обрабатывает исключение и выводит в консоль соответствующее сообщение.
     *
     * @param params Набор параметров для отправки запроса на получение пользователей
     */
    @Override
    @Transactional
    public void saveToDatabase(Map<String, String> params) {
        try {
            // Получение пользователей из API
            ResponseEntity<String> response = randomUserMeClient.getRandomUsers(params);

            // Десериализация данных из JSON в список пользователей
            List<User> users = jsonDeserializer.deserialize(response.getBody());

            List<User> savedUsers = userRepository.saveAll(users);
            log.info("Saved {} users to Database: {}", savedUsers.size(),  savedUsers);
        } catch (HttpStatusCodeException e) {
            System.out.println("\nНевозможно загрузить в БД. Сервер вернул ответ: " + e.getStatusCode());
        }
    }

    /**
     * Сначала {@link ExportStrategyFactory#createExportStrategy(String) определяется имплементация интерфейса} ExportStrategy
     * для выбора нужного метода экспорта.
     * Затем на основе количества пользователей отправляется запрос в БД для выгрузки.
     * После получения списка пользователей, они {@link ExportStrategy#export(List) отправляются на выгрузку.}
     *
     * @param exportMethod Строковое представление метода для экспорта (CSV, Excel)
     * @param numberOfUsers Количество пользователей, необходимых для выгрузки (0 = все)
     */
    @Override
    public void getFromDatabase(String exportMethod, int numberOfUsers) {
        List<User> users;
        try {
            // Выбор метода экспорта с применением паттерна проектирования 'Стратегия' и 'Фабрика' с помощью ExportStrategyFactory
            ExportStrategy exportStrategy = exportStrategyFactory.createExportStrategy(exportMethod);
            if (numberOfUsers == 0) {
                // Выгрузка всех пользователей из БД
                users = userRepository.findAllOptimized();
            } else {
                // TODO: Сделать динамическую выборку (не первых N, а по запросу из консоли)
                // Создаем объект Pageable для выборки первых N пользователей
                Pageable pageable = PageRequest.of(0, numberOfUsers);
                users = userRepository.findNOptimized(pageable).getContent();
            }

            // Проверяем на пустой список, чтобы предотвратить пустой экспорт
            if (users.isEmpty()) {
                log.debug("Cannot export users because database is empty.");
                System.out.println("\nВ БД отсутствуют пользователи, выгрузка недоступна.");
                return;
            }

            // Экспорт пользователей на основе выбранного метода
            exportStrategy.export(users);
        } catch (IllegalArgumentException e) {
            System.out.println("\nДанный метод не поддерживается.");
        }
    }
}
