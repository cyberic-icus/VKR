package ru.rsreu.groupmessageservice.service.impl;

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
import ru.rsreu.groupmessageservice.domain.GroupMessage;
import ru.rsreu.groupmessageservice.repository.GroupMessageRepository;
import ru.rsreu.groupmessageservice.service.GroupMessageService;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

@Service
@Log
public class GroupMessageServiceImpl implements GroupMessageService {

    private static SerializeConfig mapping = new SerializeConfig();
    private static String dateFormat;

    static {
        dateFormat = "yyyy-MM-dd HH:mm:ss";
        mapping.put(Timestamp.class, new SimpleDateFormatSerializer(dateFormat));
    }

    @Autowired
    GroupMessageRepository groupMessageRepository;

    @Override
    public Response addGroupMessage(Integer fromId, Integer groupId, String toId, String content, Integer type, String time) {
        try {
            GroupMessage groupMessage = new GroupMessage();
            groupMessage.setFromId(fromId);
            groupMessage.setGroupId(groupId);
            groupMessage.setToId(toId);
            groupMessage.setContent(content);
            groupMessage.setType(type);
            groupMessage.setTime(Common.getTimeFromString(time));
            groupMessageRepository.save(groupMessage);
            return new Response(1, "Добавление группового сообщения успешно", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(-1, "Ошибка при добавлении группового сообщения", null);
        }
    }

    /**
     * @param userId
     * @param id
     * @param page
     * @param number
     * @return
     */
    @Override
    public Response getMessageRecordBetweenUserAndGroup(Integer userId, Integer id, Integer page, Integer number) {
        try {
            //TODO Проверить, является ли пользователь текущим членом группы
            Pageable sortedByTime = PageRequest.of(page, number, Sort.by("time").descending());
            List<GroupMessage> groupMessages = groupMessageRepository.findAllByGroupId(id, sortedByTime);
            Collections.sort(groupMessages);
            // В следующий раз надо бы вернуть объект напрямую, чтобы проверить, можно ли его преобразовать в JSON объект
            return new Response(1, "Получение истории группы успешно", JSONArray.toJSONString(groupMessages, SerializerFeature.UseSingleQuotes));
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(-1, "Ошибка при получении истории группы", null);
        }
    }
}
