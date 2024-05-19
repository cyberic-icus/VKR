package ru.rsreu.groupmessageservice.domain;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
@Data
public class GroupMessage implements Comparable<GroupMessage> {
    @Id
    @GeneratedValue
    @Column(nullable = false, columnDefinition = "Int(11) COMMENT 'Сообщение первичный ключ'")
    private int id;
    @Column(nullable = false, columnDefinition = "Int(11) COMMENT 'ID отправителя сообщения'")
    private int fromId;
    @Column(nullable = false, columnDefinition = "Int(11) COMMENT 'ID группы получателя сообщения'")
    private int groupId;
    @Column(nullable = false, columnDefinition = "varchar(5000) COMMENT 'ID получателя сообщения (возможно не используется, если минимизировать взаимодействие с фронтендом, резервное поле)'")
    private String toId;
    @Column(nullable = false, columnDefinition = "varchar(5000) COMMENT 'Содержимое отправленного сообщения'")
    private String content;
    @Column(nullable = false, columnDefinition = "Int(11) COMMENT 'Тип сообщения'")
    private int type;
    @Column(nullable = false, columnDefinition = "datetime COMMENT 'Время отправки сообщения'")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Timestamp time;

    @Override
    public int compareTo(GroupMessage o) {
        return this.time.compareTo(o.getTime());
    }
}
