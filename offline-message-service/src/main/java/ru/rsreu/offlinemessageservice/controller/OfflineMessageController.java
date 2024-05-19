package ru.rsreu.offlinemessageservice.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.rsreu.commonservice.util.Response;
import ru.rsreu.offlinemessageservice.service.message.OfflineMessageService;

@RestController
@Api(value = "Контроллер офлайн сообщений", description = "Используется для обработки офлайн сообщений, таких как добавление, удаление, изменение и поиск")
@RequestMapping(value = "/v1")
public class OfflineMessageController {

    @Autowired
    OfflineMessageService offlineMessageService;

    @ApiOperation(value = "Добавить запись офлайн сообщения", notes = "Добавление офлайн сообщения чата", response = Response.class)
    @PostMapping(value = "/offlineMessage")
    public Response addOfflineMessage(@RequestParam("fromId") Integer fromId, @RequestParam("toId") Integer toId,
                                      @RequestParam("content") String content, @RequestParam("type") Integer type,
                                      @RequestParam("time") String time) {
        return offlineMessageService.addOfflineMessage(fromId, toId, content, type, time);
    }

    @ApiOperation(value = "Получить офлайн сообщения", notes = "Получение офлайн сообщений", response = Response.class)
    @GetMapping(value = "/offlineMessages/toId/{userId}")
    public Response getOfflineMessage(@PathVariable("userId") Integer userId) {
        return offlineMessageService.getOfflineMessageTo(userId);
    }

//    @ApiOperation(value = "Добавить запись офлайн сообщения", notes = "Добавление офлайн сообщения чата", response = Response.class)
//    @GetMapping(value = "/offlineMessages/toId/{userId}")
//    public Response addMessage(@PathVariable("userId") Integer userId) {
//        return offlineMessageService.getOfflineMessageTo(userId);
//    }
}
