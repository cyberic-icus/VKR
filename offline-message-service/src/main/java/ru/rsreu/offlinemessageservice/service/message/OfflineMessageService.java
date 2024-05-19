package ru.rsreu.offlinemessageservice.service.message;

import ru.rsreu.commonservice.util.Response;

public interface OfflineMessageService {
    Response addOfflineMessage(Integer fromId, Integer toId, String content, Integer type, String time);

    Response getOfflineMessageTo(Integer userId);

//    Response getOfflineMessageFrom(Integer userId);
}
