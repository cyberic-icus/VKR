package ru.rsreu.offlinemessageservice.service.groupmessage;

import ru.rsreu.commonservice.util.Response;

public interface OfflineGroupMessageService {
    Response addOfflineGroupMessage(Integer fromId, Integer groupId, Integer toId, String content, Integer type, String time);
}
