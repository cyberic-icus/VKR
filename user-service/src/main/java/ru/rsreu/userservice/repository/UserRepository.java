package ru.rsreu.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.rsreu.userservice.domain.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUserId(Integer userId);

    User findByUserName(String userName);
}
