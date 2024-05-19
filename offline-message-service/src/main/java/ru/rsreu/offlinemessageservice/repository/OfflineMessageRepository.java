package ru.rsreu.offlinemessageservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.rsreu.offlinemessageservice.domain.OfflineMessage;

import java.util.List;

public interface OfflineMessageRepository extends JpaRepository<OfflineMessage, Integer> {
    List<OfflineMessage> findAllByToId(Integer toId);

    //    List<OfflineMessage> findAllByFromId(Integer fromId);
    void deleteAllByToId(Integer toId);

    OfflineMessage findByFromIdAndToIdAndType(Integer fromId, Integer toId, Integer type);
}
