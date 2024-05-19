package ru.rsreu.messageservice.repository;


import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.rsreu.messageservice.domain.Message;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Integer> {
    List<Message> findAllByFromIdAndToId(Integer fromId, Integer toId);

    List<Message> findAllByFromIdAndToIdOrFromIdAndToId(Integer fromId, Integer toId, Integer fromId2, Integer toId2, Pageable pageable);

    List<Message> findAllByToIdAndType(Integer toId, Integer type);

    List<Message> findAllByToId(Integer toId);
}
