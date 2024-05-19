package ru.rsreu.userservice.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.rsreu.commonservice.util.Response;
import ru.rsreu.userservice.service.UserService;

/**
 * Логин и регистрация временно остаются в user-service. В дальнейшем их следует выделить отдельно, оставив аутентификацию интерфейса
 */
@RestController
@Api(value = "Класс управления пользователями", description = "Используется для обработки регистрации пользователей, входа в систему и CRUD операций")
@RequestMapping(value = "/v1")
public class UserController {

    @Autowired
    UserService userService;

    /**
     * Временно оставляем
     *
     * @param
     * @return
     */
    @ApiOperation(value = "Интерфейс входа пользователя", notes = "Обработка входа пользователя", response = Response.class)
    @GetMapping(value = "/user/login")
    public Response login(@RequestParam("userName") String userName, @RequestParam("userPassword") String userPassword) {
        return userService.login(userName, userPassword);
    }

    @ApiOperation(value = "Интерфейс выхода пользователя", notes = "Выход пользователя из системы", response = Response.class)
    @GetMapping(value = "/user/logout")
    public Response logout(@RequestParam("token") String token) {
        return userService.logout(token);
    }

    @ApiOperation(value = "Интерфейс регистрации пользователя", notes = "Обработка регистрации пользователя", response = Response.class)
    @PostMapping(value = "/user/register")
    public Response register(@RequestParam("userName") String userName,
                             @RequestParam("userNickName") String userNickName,
                             @RequestParam("userPassword") String userPassword) {
        return userService.register(userName, userNickName, userPassword);
    }

    @ApiOperation(value = "Получение друзей пользователя", notes = "Получение всех друзей пользователя", response = Response.class)
    @GetMapping(value = "/users/friends/{userId}")
    public Response getRelations(@PathVariable("userId") Integer userId) {
        return userService.getRelations(userId);
    }

    @ApiOperation(value = "Получение групп пользователя", notes = "Получение всех групп пользователя", response = Response.class)
    @GetMapping(value = "/users/groups/{userId}")
    public Response getUserGroups(@PathVariable("userId") Integer userId) {
        return userService.getUserGroups(userId);
    }

    @ApiOperation(value = "Разбор токена для получения текущего пользователя", notes = "Получение ID текущего пользователя", response = Response.class)
    @GetMapping(value = "/user/token/{token}")
    public Response getCurrentUser(@PathVariable("token") String token) {
        return userService.getCurrentUser(token);
    }

    @ApiOperation(value = "Получение текущего пользователя по имени", notes = "Получение ID текущего пользователя по имени", response = Response.class)
    @GetMapping(value = "/user/userName/{userName}")
    public Response findUserByName(@PathVariable("userName") String userName) {
        return userService.findUserByName(userName);
    }

    @ApiOperation(value = "Получение текущего пользователя по ID", notes = "Получение текущего пользователя по ID", response = Response.class)
    @GetMapping(value = "/user/id/{id}")
    public Response findUserById(@PathVariable("id") String id) {
        return userService.findUserById(id);
    }

    @ApiOperation(value = "Обновление полей кеша отношений друзей", notes = "Обновление отношений друзей", response = Response.class)
    @PostMapping(value = "/user/userRelations")
    public Response updateUserRelation(@RequestParam("userIdA") Integer userIdA, @RequestParam("userIdB") Integer userIdB) {
        return userService.updateUserRelation(userIdA, userIdB);
    }

    @ApiOperation(value = "Обновление полей кеша групп пользователя", notes = "Обновление групп пользователя", response = Response.class)
    @PostMapping(value = "/user/userGroups")
    public Response updateUserGroup(@RequestParam("userId") Integer userId, @RequestParam("groupId") Integer groupId) {
        return userService.updateUserGroup(userId, groupId);
    }

    @ApiOperation(value = "Получение списка пользователей по строке ID", notes = "Получение списка пользователей", response = Response.class)
    @GetMapping(value = "/users/{userIds}")
    public Response getUsersByUserIds(@PathVariable("userIds") String userIds) {
        return userService.getUsersByUserIds(userIds);
    }

}
