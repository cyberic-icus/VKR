package ru.rsreu.offlinemessageservice.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.rsreu.commonservice.util.Response;
import ru.rsreu.offlinemessageservice.service.groupmessage.OfflineGroupMessageService;

@RestController
@Api(value = "Контроллер офлайн сообщений группы", description = "Используется для обработки офлайн сообщений группы, таких как добавление, удаление, изменение и поиск")
@RequestMapping(value = "/v1")
public class OfflineGroupMessageController {

    @Autowired
    OfflineGroupMessageService offlineGroupMessageService;

    @ApiOperation(value = "Добавить запись офлайн сообщения", notes = "Добавление офлайн сообщения чата", response = Response.class)
    @PostMapping(value = "/offlineGroupMessage")
    public Response addOfflineMessage(@RequestParam("fromId") Integer fromId, @RequestParam("groupId") Integer groupId,
                                      @RequestParam("toId") Integer toId, @RequestParam("content") String content,
                                      @RequestParam("type") Integer type, @RequestParam("time") String time) {
        return offlineGroupMessageService.addOfflineGroupMessage(fromId, groupId, toId, content, type, time);
    }
}
