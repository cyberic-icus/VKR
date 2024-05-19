package ru.rsreu.groupservice.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.rsreu.groupservice.domain.UserGroupRelation;
import ru.rsreu.groupservice.domain.prikey.UserGroupRelationPriKey;

import java.util.List;

public interface UserGroupRelationRepository extends JpaRepository<UserGroupRelation, UserGroupRelationPriKey> {
    List<UserGroupRelation> findByGroupId(Integer groupId);
}
