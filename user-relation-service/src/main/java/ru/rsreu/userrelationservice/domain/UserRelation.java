package ru.rsreu.userrelationservice.domain;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import ru.rsreu.userrelationservice.domain.prikey.UserRelationPriKey;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.sql.Timestamp;

@Entity
@Data
@IdClass(UserRelationPriKey.class)
public class UserRelation {
    @Id
    @Column(nullable = false, columnDefinition = "Int(11) COMMENT 'ID пользователя A'")
    private int userIdA;

    @Id
    @Column(nullable = false, columnDefinition = "Int(11) COMMENT 'ID пользователя B'")
    private int userIdB;

    @Column(nullable = false, columnDefinition = "Int(11) COMMENT 'Статус отношения'")
    private int relationStatus;

    @Column(nullable = false, columnDefinition = "datetime COMMENT 'Время установления связи'")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Timestamp relationStart;
}
