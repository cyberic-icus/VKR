package ru.rsreu.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.rsreu.userservice.domain.UserDetail;

public interface UserDetailRepository extends JpaRepository<UserDetail, Integer> {
    UserDetail findByUserDetailName(String userDetailName);
}
