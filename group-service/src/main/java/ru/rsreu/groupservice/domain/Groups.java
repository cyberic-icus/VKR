package ru.rsreu.groupservice.domain;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
@Data
public class Groups {
    @Id
    @GeneratedValue
    @Column(nullable = false, columnDefinition = "Int(11) COMMENT 'Основной ключ группы'")
    private int id;
    @Column(nullable = false, columnDefinition = "varchar(10) COMMENT 'Номер группы'")
    private String groupId;
    @Column(nullable = false, columnDefinition = "varchar(20) COMMENT 'Название группы'")
    private String groupName;
    @Column(nullable = false, columnDefinition = "Int(11) COMMENT 'ID создателя группы'")
    private int groupCreatorId;
    @Column(nullable = false, columnDefinition = "datetime COMMENT 'Время создания'")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Timestamp groupCreateTime;
    @Column(nullable = true, columnDefinition = "varchar(1000) COMMENT 'Описание группы'")
    private String groupIntroduction;
    @Column(nullable = false, columnDefinition = "Int(11) COMMENT 'Количество участников'")
    private int groupUserCount;
    @Column(nullable = true, columnDefinition = "varchar(5000) COMMENT 'Участники группы'")
    private String groupMembers;
}