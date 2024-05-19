package ru.rsreu.userrelationservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.rsreu.userrelationservice.domain.UserRelation;
import ru.rsreu.userrelationservice.domain.prikey.UserRelationPriKey;

public interface UserRelationRepository extends JpaRepository<UserRelation, UserRelationPriKey> {
}
