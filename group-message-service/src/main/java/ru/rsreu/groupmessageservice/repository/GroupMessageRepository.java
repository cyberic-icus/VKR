package ru.rsreu.groupmessageservice.repository;


import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.rsreu.groupmessageservice.domain.GroupMessage;

import java.util.List;

public interface GroupMessageRepository extends JpaRepository<GroupMessage, Integer> {

    List<GroupMessage> findAllByGroupId(Integer id, Pageable pageable);

    List<GroupMessage> findAllByFromIdAndToId(Integer fromId, Integer toId);

    List<GroupMessage> findAllByToIdAndType(Integer toId, Integer type);

    List<GroupMessage> findAllByToId(Integer toId);
}
