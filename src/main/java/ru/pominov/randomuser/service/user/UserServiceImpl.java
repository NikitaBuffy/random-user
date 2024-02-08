package ru.pominov.randomuser.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pominov.randomuser.model.user.User;
import ru.pominov.randomuser.repository.UserRepository;
import ru.pominov.randomuser.util.JsonDeserializer;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final JsonDeserializer jsonDeserializer;
    private final UserRepository userRepository;

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
    public void getFromDatabase() {
    }
}
