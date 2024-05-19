package ru.rsreu.offlinemessageservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.rsreu.offlinemessageservice.domain.OfflineGroupMessage;

import java.util.List;

public interface OfflineGroupMessageRepository extends JpaRepository<OfflineGroupMessage, Integer> {
    List<OfflineGroupMessage> findAllByToId(Integer userId);

    void deleteAllByToId(Integer userId);
}
