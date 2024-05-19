package ru.rsreu.userservice.domain;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
@Data
public class UserDetail {
    @Id
    @GeneratedValue
    @Column(nullable = false, columnDefinition = "Int(11) COMMENT 'Основной ключ деталей пользователя'")
    private int userDetailId;
    @Column(nullable = false, columnDefinition = "varchar(40) COMMENT 'Имя пользователя'")
    private String userDetailName;
    @Column(nullable = false, columnDefinition = "varchar(40) COMMENT 'Никнейм пользователя'")
    private String userDetailNickName;
    @Column(nullable = false, columnDefinition = "varchar(40) COMMENT 'Пароль пользователя'")
    private String userDetailPassword;
    @Column(nullable = true, columnDefinition = "varchar(20) COMMENT 'Электронная почта пользователя'")
    private String userMailNumber;
    @Column(nullable = true, columnDefinition = "varchar(15) COMMENT 'Номер телефона пользователя'")
    private String userPhoneNumber;
    @Column(nullable = false, columnDefinition = "datetime COMMENT 'Время регистрации'")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Timestamp userRegisterTime;

}
