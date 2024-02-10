package ru.pominov.randomuser.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.pominov.randomuser.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    /* Стандартным методом findAll Hibernate делает несколько запросов к таблицам
     * users, user_login и user_location.
     * Для оптимизации запросов применяем JOIN, чтобы общий запрос к БД был выполнен один раз
     */
    @Query("SELECT u FROM User u " +
            "LEFT JOIN FETCH u.userLocation " +
            "LEFT JOIN FETCH u.userLogin")
    List<User> findAllOptimized();

    /* Используем тот же запрос с JOIN с использованием Pageable.
     * Этот подход позволяет избежать проблемы N + 1 у Hibernate.
     * Теперь вместо одного запроса к users с offset и N количества запросов к смежным таблицам user_login и user_location,
     * выполняется один запрос с JOIN
     */
    @Query("SELECT u FROM User u " +
            "LEFT JOIN FETCH u.userLocation " +
            "LEFT JOIN FETCH u.userLogin")
    Page<User> findNOptimized(Pageable pageable);
}
