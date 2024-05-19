package ru.rsreu.messageservice.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.rsreu.commonservice.util.Response;
import ru.rsreu.messageservice.domain.Message;
import ru.rsreu.messageservice.service.MessageService;

import java.util.List;

@RestController
@Api(value = "Контроллер сообщений", description = "Используется для обработки операций с сообщениями, таких как добавление, удаление, изменение и поиск")
@RequestMapping(value = "/v1")
public class MessageController {

    @Autowired
    MessageService messageService;

    @ApiOperation(value = "Добавить запись сообщения", notes = "Добавление записи чата", response = Response.class)
    @PostMapping(value = "/message")
    public Response addMessage(@RequestParam("fromId") Integer fromId, @RequestParam("toId") Integer toId,
                               @RequestParam("content") String content, @RequestParam("type") Integer type,
                               @RequestParam("time") String time) {
        return messageService.addMessage(fromId, toId, content, type, time);
    }

    @ApiOperation(value = "Получить записи сообщений между пользователями A и B", notes = "Получение записей чата между двумя пользователями", response = Response.class)
    @GetMapping(value = "/messages/{userIdA}/{userIdB}/{page}/{number}")
    public Response getMessageRecordBetweenUsers(@PathVariable(value = "userIdA") Integer userIdA, @PathVariable("userIdB") Integer userIdB,
                                                 @PathVariable(value = "page") Integer page, @PathVariable(value = "number") Integer number) {
        return messageService.getMessageRecordBetweenUsers(userIdA, userIdB, page, number);
    }

    /**
     * Пакетное добавление записей чата
     *
     * @param messages
     * @return
     */
    @ApiOperation(value = "Добавить записи сообщений", notes = "Добавление записей чата", response = Response.class)
    @PostMapping(value = "/messages")
    public Response updateMessage(@RequestParam("messages") List<Message> messages) {
        return messageService.addMessages(messages);
    }

}
