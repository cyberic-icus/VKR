package ru.rsreu.groupservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.rsreu.groupservice.domain.Groups;

import java.util.List;

public interface GroupRepository extends JpaRepository<Groups, Integer> {
    Groups findByGroupId(String groupId);

    List<Groups> findByIdIn(List<Integer> ids);
}
