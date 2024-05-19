package ru.rsreu.groupservice.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.rsreu.commonservice.util.Response;
import ru.rsreu.groupservice.service.group.GroupService;

@RestController
@Api(value = "Контроллер группы", description = "Используется для обработки операций, связанных с группами, таких как добавление, удаление, изменение и поиск")
@RequestMapping(value = "/v1")
public class GroupController {

    @Autowired
    GroupService groupService;

    @ApiOperation(value = "Получить всех пользователей группы", notes = "Получить всех пользователей группы", response = Response.class)
    @GetMapping(value = "/group/users/{id}")
    public Response getGroupUsers(@PathVariable("id") Integer id) {
        return groupService.getGroupUsers(id);
    }

    @ApiOperation(value = "Создать группу", notes = "Создать группу", response = Response.class)
    @PostMapping(value = "/group")
    public Response createGroup(@RequestParam("groupName") String groupName,
                                @RequestParam("groupIntroduction") String groupIntroduction,
                                @RequestParam("groupCreatorId") Integer groupCreatorId) {
        return groupService.createGroup(groupName, groupIntroduction, groupCreatorId);
    }

    @ApiOperation(value = "Получить группу по ID", notes = "Получить группу по ID", response = Response.class)
    @GetMapping(value = "/group/{id}")
    public Response findGroupById(@PathVariable("id") Integer id) {
        return groupService.findGroupById(id);
    }

    @ApiOperation(value = "Получить группы по ID", notes = "Получить группы по ID", response = Response.class)
    @GetMapping(value = "/groups/{ids}")
    public Response findGroupById(@PathVariable("ids") String ids) {
        return groupService.findGroupByIds(ids);
    }
}