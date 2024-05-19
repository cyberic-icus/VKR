package ru.rsreu.offlinemessageservice.service.groupmessage.impl;

import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.rsreu.commonservice.util.Common;
import ru.rsreu.commonservice.util.Response;
import ru.rsreu.offlinemessageservice.domain.OfflineGroupMessage;
import ru.rsreu.offlinemessageservice.repository.OfflineGroupMessageRepository;
import ru.rsreu.offlinemessageservice.service.groupmessage.OfflineGroupMessageService;

import java.sql.Timestamp;

@Service
public class OfflineGroupMessageServiceImpl implements OfflineGroupMessageService {

    private static SerializeConfig mapping = new SerializeConfig();
    private static String dateFormat;

    static {
        dateFormat = "yyyy-MM-dd HH:mm:ss";
        mapping.put(Timestamp.class, new SimpleDateFormatSerializer(dateFormat));
    }

    @Autowired
    OfflineGroupMessageRepository offlineGroupMessageRepository;

    @Override
    public Response addOfflineGroupMessage(Integer fromId, Integer groupId, Integer toId, String content, Integer type, String time) {
        try {
            OfflineGroupMessage offlineGroupMessage = new OfflineGroupMessage();
            offlineGroupMessage.setFromId(fromId);
            offlineGroupMessage.setGroupId(groupId);
            offlineGroupMessage.setToId(toId);
            offlineGroupMessage.setContent(content);
            offlineGroupMessage.setType(type);
            offlineGroupMessage.setTime(Common.getTimeFromString(time));
            offlineGroupMessageRepository.save(offlineGroupMessage);
            return new Response(1, "Insert offline group message success", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(-1, "Insert offline group message error", null);
        }
    }
}
