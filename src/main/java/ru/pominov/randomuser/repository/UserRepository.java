package ru.pominov.randomuser.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.pominov.randomuser.model.user.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findFirstById(int numberOfUsers);
}
