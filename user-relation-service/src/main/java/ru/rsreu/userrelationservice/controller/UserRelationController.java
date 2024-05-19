package ru.rsreu.userrelationservice.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.rsreu.commonservice.util.Response;
import ru.rsreu.userrelationservice.service.UserRelationService;

@RestController
@Api(value = "Контроллер отношений пользователей", description = "Используется для обработки операций с отношениями пользователей, таких как добавление, удаление, изменение и поиск")
@RequestMapping(value = "/v1")
public class UserRelationController {

    @Autowired
    UserRelationService userRelationService;

    @ApiOperation(value = "Добавить друга", notes = "Добавление друга в отношения", response = Response.class)
    @PostMapping(value = "/userRelations")
    public Response buildRelation(@RequestParam("userIdA") Integer userIdA, @RequestParam("userIdB") Integer userIdB) {
        return userRelationService.buildRelation(userIdA, userIdB);
    }
}
