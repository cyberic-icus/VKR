package ru.rsreu.offlinemessageservice.service.message.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import ru.rsreu.commonservice.util.Common;
import ru.rsreu.commonservice.util.Response;
import ru.rsreu.offlinemessageservice.domain.OfflineGroupMessage;
import ru.rsreu.offlinemessageservice.domain.OfflineMessage;
import ru.rsreu.offlinemessageservice.repository.OfflineGroupMessageRepository;
import ru.rsreu.offlinemessageservice.repository.OfflineMessageRepository;
import ru.rsreu.offlinemessageservice.service.message.OfflineMessageService;

import java.sql.Timestamp;
import java.util.List;

@Service
public class OfflineMessageServiceImpl implements OfflineMessageService {

    private static SerializeConfig mapping = new SerializeConfig();
    private static String dateFormat;

    static {
        dateFormat = "yyyy-MM-dd HH:mm:ss";
        mapping.put(Timestamp.class, new SimpleDateFormatSerializer(dateFormat));
    }

    @Autowired
    RestTemplate restTemplate;
    @Autowired
    OfflineGroupMessageRepository offlineGroupMessageRepository;
    @Autowired
    OfflineMessageRepository offlineMessageRepository;

    @Override
    public Response addOfflineMessage(Integer fromId, Integer toId, String content, Integer type, String time) {
        try {
            OfflineMessage message = offlineMessageRepository.findByFromIdAndToIdAndType(fromId, toId, type);
            if (Common.isNull(message)) {
                OfflineMessage offlineMessage = new OfflineMessage();
                offlineMessage.setFromId(fromId);
                offlineMessage.setToId(toId);
                offlineMessage.setContent(content);
                offlineMessage.setType(type);
                offlineMessage.setTime(Common.getTimeFromString(time));
                offlineMessageRepository.save(offlineMessage);
            } else {
                Integer count = Integer.valueOf(message.getContent());
                message.setContent(String.valueOf(++count));
                offlineMessageRepository.save(message);
            }

            return new Response(1, "Insert offline message success", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(-1, "Insert offline message error", null);
        }
    }

    @Override
    @Transactional
    public Response getOfflineMessageTo(Integer userId) {
        try {
            List<OfflineMessage> messages = offlineMessageRepository.findAllByToId(userId);

            offlineMessageRepository.deleteAllByToId(userId);
//            Response response = restTemplate.postForEntity(CONSTANT.MESSAGE_SERVICE_ADD_MESSAGES, messages, Response.class).getBody();
//            if(response.getStatus() != 1){
//                throw new Exception();
//            }

            List<OfflineGroupMessage> groupMessages = offlineGroupMessageRepository.findAllByToId(userId);
            for (OfflineGroupMessage offlineGroupMessage : groupMessages) {
                messages.add(new OfflineMessage(offlineGroupMessage));
            }
            offlineMessageRepository.deleteAllByToId(userId);
            return new Response(1, "Get offline messages success", JSONArray.toJSONString(messages, SerializerFeature.UseSingleQuotes));
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(-1, "Get offline messages error", null);
        }
    }

//    @Override
//    public Response getOfflineMessageFrom(Integer userId) {
//        return null;
//    }
}
