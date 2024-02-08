package ru.pominov.randomuser.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.pominov.randomuser.model.user.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
