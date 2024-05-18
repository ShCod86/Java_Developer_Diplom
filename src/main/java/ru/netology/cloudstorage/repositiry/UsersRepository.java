package ru.netology.cloudstorage.repositiry;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.netology.cloudstorage.entity.User;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByLoginAndPassword(String login, String password);
}
