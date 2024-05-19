package ru.rsreu.groupservice.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.rsreu.commonservice.util.Response;
import ru.rsreu.groupservice.service.usergroup.UserGroupRelationService;

@RestController
@Api(value = "Контроллер пользователя", description = "Используется для обработки регистрации, входа и операций CRUD для пользователей")
@RequestMapping(value = "/v1")
public class UserGroupRelationController {

    @Autowired
    UserGroupRelationService userGroupRelationService;

    @ApiOperation(value = "Добавить в группу", notes = "Добавление нового члена в группу", response = Response.class)
    @PostMapping(value = "/group/user")
    public Response addGroupUsers(@RequestParam("id") Integer id, @RequestParam("userId") Integer userId) {
        return userGroupRelationService.addGroupUsers(id, userId);
    }
}