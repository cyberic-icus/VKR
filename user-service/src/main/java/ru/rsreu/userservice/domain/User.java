package ru.rsreu.userservice.domain;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class User {
    @Id
    @GenericGenerator(name = "generator", strategy = "assigned")
    @GeneratedValue(generator = "generator")
    @Column(nullable = false, columnDefinition = "Int(11) COMMENT 'Основной ключ пользователя'")
    private int userId;
    @Column(nullable = false, columnDefinition = "varchar(40) COMMENT 'Имя пользователя'")
    private String userName;
    @Column(nullable = false, columnDefinition = "varchar(40) COMMENT 'Никнейм пользователя'")
    private String userNickName;
    @Column(nullable = true, columnDefinition = "varchar(50) COMMENT 'Путь к аватару пользователя'")
    private String userImgPath;
    @Column(nullable = false, columnDefinition = "Int(11) COMMENT 'Есть ли оффлайн сообщения'")
    private int hasOffLineMessage;
    @Column(nullable = true, columnDefinition = "varchar(5000) COMMENT 'Список контактов'")
    private String userRelations;
    @Column(nullable = true, columnDefinition = "varchar(5000) COMMENT 'Список групп'")
    private String userGroups;
    @Column(nullable = false, columnDefinition = "Int(11) COMMENT 'Роль пользователя (оставлено для использования)'")
    private int userRole;
}
