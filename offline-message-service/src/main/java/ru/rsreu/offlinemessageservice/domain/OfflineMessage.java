package ru.rsreu.offlinemessageservice.domain;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
@Data
@NoArgsConstructor
public class OfflineMessage {
    @Id
    @GeneratedValue
    @Column(nullable = false, columnDefinition = "Int(11) COMMENT 'Сообщение первичный ключ'")
    private int id;

    @Column(nullable = false, columnDefinition = "Int(11) COMMENT 'ID отправителя сообщения'")
    private int fromId;

    @Column(nullable = false, columnDefinition = "Int(11) COMMENT 'ID получателя сообщения'")
    private int toId;

    @Column(nullable = false, columnDefinition = "varchar(5000) COMMENT 'Содержимое отправленного сообщения'")
    private String content;

    @Column(nullable = false, columnDefinition = "Int(11) COMMENT 'Тип сообщения'")
    private int type;

    @Column(nullable = false, columnDefinition = "datetime COMMENT 'Время отправки сообщения'")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Timestamp time;

    public OfflineMessage(OfflineGroupMessage offlineGroupMessage) {
        this.fromId = offlineGroupMessage.getFromId();
        this.toId = offlineGroupMessage.getToId();
        this.content = offlineGroupMessage.getContent();
        this.type = offlineGroupMessage.getType();
        this.time = offlineGroupMessage.getTime();
    }
}
