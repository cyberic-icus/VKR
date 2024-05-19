package ru.rsreu.groupservice.domain;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import ru.rsreu.groupservice.domain.prikey.UserGroupRelationPriKey;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.sql.Timestamp;

@Entity
@Data
@IdClass(UserGroupRelationPriKey.class)
public class UserGroupRelation {
    @Id
    @Column(nullable = false, columnDefinition = "Int(11) COMMENT 'Внешний ключ пользователя'")
    private int userId;
    @Id
    @Column(nullable = false, columnDefinition = "Int(11) COMMENT 'Внешний ключ группы'")
    private int groupId;
    @Column(nullable = false, columnDefinition = "Int(11) COMMENT 'Уровень группы'")
    private int groupLevel;
    @Column(nullable = false, columnDefinition = "varchar(50) COMMENT 'Псевдоним в группе'")
    private String groupUserNickName;
    @Column(nullable = false, columnDefinition = "datetime COMMENT 'Время входа в группу'")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Timestamp enterGroupTime;
}