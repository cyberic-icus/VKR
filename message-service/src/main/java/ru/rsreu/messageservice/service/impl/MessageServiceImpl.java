package ru.rsreu.messageservice.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.rsreu.commonservice.util.Common;
import ru.rsreu.commonservice.util.Response;
import ru.rsreu.messageservice.domain.Message;
import ru.rsreu.messageservice.repository.MessageRepository;
import ru.rsreu.messageservice.service.MessageService;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

@Service
@Log
public class MessageServiceImpl implements MessageService {

    private static SerializeConfig mapping = new SerializeConfig();
    private static String dateFormat;

    static {
        dateFormat = "yyyy-MM-dd HH:mm:ss";
        mapping.put(Timestamp.class, new SimpleDateFormatSerializer(dateFormat));
    }

    @Autowired
    MessageRepository messageRepository;

    @Override
    public Response addMessage(Integer fromId, Integer toId, String content, Integer type, String time) {
        try {
            Message message = new Message();
            message.setFromId(fromId);
            message.setToId(toId);
            message.setContent(content);
            message.setType(type);
            message.setTime(Common.getTimeFromString(time));
            messageRepository.save(message);
            log.info("Я вставил сообщение " + message.toString() + " в message-mysql");
            return new Response(1, "Добавление сообщения успешно", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(-1, "Ошибка при добавлении сообщения", null);
        }
    }

    @Override
    public Response getMessageRecordBetweenUsers(Integer userIdA, Integer userIdB, Integer page, Integer number) {
        try {
            Pageable sortedByTime = PageRequest.of(page, number, Sort.by("time").descending());
            List<Message> messages = messageRepository.findAllByFromIdAndToIdOrFromIdAndToId(userIdA, userIdB, userIdB, userIdA, sortedByTime);
            Collections.sort(messages);
            return new Response(1, "Получение записей сообщений между пользователями успешно", JSONArray.toJSONString(messages, SerializerFeature.UseSingleQuotes));
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(-1, "Ошибка при получении записей сообщений между пользователями", null);
        }
    }

    @Override
    public Response addMessages(List<Message> messages) {
        try {
            for (Message message : messages) {
                messageRepository.save(message);
            }
            return new Response(1, "Добавление сообщений успешно", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(-1, "Ошибка при добавлении сообщений", null);
        }
    }

//    /**
//     * Этот метод тоже пока оставлю, может и не понадобится, так как лучше не сохранять не пересланные сообщения в таблицу сообщений, а лучше добавить таблицу офлайн-сообщений для удобства поиска
//     * @param id
//     * @param isTransport
//     * @return
//     */
//    @Override
//    public Response updateMessage(Integer id, Integer isTransport) {
//        try {
//            Message message = messageRepository.getOne(id);
//            message.setIsTransport(isTransport);
//            messageRepository.save(message);
//            return new Response(1, "Обновление сообщения успешно", null);
//        } catch (Exception e){
//            e.printStackTrace();
//            return new Response(1, "Ошибка при обновлении сообщения", null);
//        }
//    }
//
//    /**
//     * Пока оставлю
//     * @param jsonObj
//     * @return
//     */
//    @Override
//    public Response getMessageRecordBetweenUserAndGroup(String jsonObj) {
//        try {
//            JSONObject jsonObject = JSONObject.parseObject(jsonObj);
//            Integer id = jsonObject.getInteger("id");
//            Integer userId = jsonObject.getInteger("userId");
//            List<Message> messages = messageRepository.findAllByToIdAndTypeAndIsTransport(id, 2, 1);
//            for (Message message : messages) {
//                message.setType(1);
//                message.setToId(userId);
//            }
//            return new Response(1, "Получение записей чата группы успешно", JSONArray.toJSONString(messages, SerializerFeature.UseSingleQuotes));
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new Response(-1, "Ошибка при получении записей чата группы", null);
//        }
//    }
}
