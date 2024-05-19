package ru.rsreu.messageservice.service;

import ru.rsreu.commonservice.util.Response;
import ru.rsreu.messageservice.domain.Message;

import java.util.List;

public interface MessageService {
    Response addMessage(Integer fromId, Integer toId, String content, Integer type, String time);

    Response getMessageRecordBetweenUsers(Integer userIdA, Integer userIdB, Integer page, Integer number);

    Response addMessages(List<Message> messages);
//    Response updateMessage(Integer id, Integer isTransport);
}
