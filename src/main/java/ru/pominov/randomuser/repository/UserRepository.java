package ru.pominov.randomuser.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.pominov.randomuser.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    /* Стандартным методом findAll Hibernate делает несколько запросов к таблицам
     * users, user_pictures, user_login и user_location.
     * Для оптимизации запросов применяем JOIN, чтобы общий запрос к БД был выполнен один раз
     */
    @Query("SELECT u FROM User u " +
            "LEFT JOIN FETCH u.userPicture " +
            "LEFT JOIN FETCH u.userLocation " +
            "LEFT JOIN FETCH u.userLogin")
    List<User> findAllOptimized();
}
