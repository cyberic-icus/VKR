package ru.rsreu.websocketserverservice.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.java.Log;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.rsreu.commonservice.dto.AuthUser;
import ru.rsreu.commonservice.util.Common;
import ru.rsreu.commonservice.util.JwtUtils;
import ru.rsreu.commonservice.util.Response;
import ru.rsreu.websocketserverservice.domain.dto.Message;
import ru.rsreu.websocketserverservice.service.MessageService;

import static ru.rsreu.commonservice.util.Common.isNull;
import static ru.rsreu.websocketserverservice.constant.CONSTANT.*;
import static ru.rsreu.websocketserverservice.constant.Global.*;

@Log
public class MessageServiceImpl implements MessageService {

    AuthUser user = new AuthUser();

    @Override
    public void onOpen(ChannelHandlerContext ctx, FullHttpRequest req) {
        // Проверка токена
        String token = req.getUri().replace("/", "");
        user = JwtUtils.parseJWT(token);
        if (user == null) {
            log.info("Проверка токена пользователя не удалась, пожалуйста, войдите снова!");
            return;
        }
        onLineList.put(user.getUserId(), ctx.channel());
        onLineNumber++;
    }

    @Override
    public void onClose() {
        onLineList.remove(user.getUserId());
        onLineNumber--;
    }

    @Override
    public void onMessage(ChannelHandlerContext ctx, String jsonMessage) {
        log.info("Получил сообщение " + jsonMessage);
        Message message = new Message();
        JSONObject jsonObjectMessage = JSON.parseObject(jsonMessage);
        message.setFromId(jsonObjectMessage.getInteger("from"));
        message.setContent(jsonObjectMessage.getString("content"));
        message.setType(jsonObjectMessage.getInteger("type"));
        message.setTime(Common.getCurrentTime());
        JSONArray users = jsonObjectMessage.getJSONArray("to");
        Channel self = getChannel(message.getFromId());
        if (message.getType() == 0) {
            sendMessage(self, jsonMessage);
        }
        if (message.getType() == 1) {
            message.setToId(users.getInteger(0));
            saveGroupMessage(message, JSONArray.toJSONString(users));
            for (int i = 1; i < users.size(); i++) {
                message.setToId(users.getInteger(i));
                Channel channel = getChannel(message.getToId());
                if (!isNull(channel)) {
                    sendMessage(channel, jsonMessage);
                } else {
                    message.setFromId(users.getInteger(0));
                    message.setContent("1");
                    message.setType(7);
                    saveMessage(message, OFFLINE_MESSAGE_BASE, ADD_OFFLINE_MESSAGE);
                }

                log.info("Отправил сообщение " + message.getToId());
            }

        } else {
            if (users.size() > 0) {
                message.setToId(users.getInteger(0));
                Channel channel = getChannel(message.getToId());
                if (!isNull(channel)) {
                    saveMessage(message, MESSAGE_BASE, ADD_MESSAGE);
                    sendMessage(channel, jsonMessage);
                } else {
                    saveMessage(message, MESSAGE_BASE, ADD_MESSAGE);
                    saveMessage(message, OFFLINE_MESSAGE_BASE, ADD_OFFLINE_MESSAGE);
                }
                log.info("Отправил сообщение " + message.getToId());
            }
        }
    }

    private void sendMessage(Channel channel, String message) {
        channel.write(new TextWebSocketFrame(message));
        channel.flush();
    }

    private Channel getChannel(Integer userId) {
        return onLineList.get(userId);
    }

    private void saveMessage(Message message, String baseUrl, String path) {
        MultiValueMap<String, Object> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("fromId", message.getFromId());
        requestParams.add("toId", message.getToId());
        requestParams.add("content", message.getContent());
        requestParams.add("type", message.getType());
        requestParams.add("time", sdf.format(message.getTime()));

        sendSyncHttpRequest(baseUrl, path, requestParams);
    }

    private void saveGroupMessage(Message message, String toId) {
        MultiValueMap<String, Object> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("fromId", message.getFromId());
        requestParams.add("groupId", message.getToId());
        requestParams.add("toId", toId);
        requestParams.add("content", message.getContent());
        requestParams.add("type", message.getType());
        requestParams.add("time", sdf.format(message.getTime()));
        sendSyncHttpRequest(GROUP_MESSAGE_BASE, ADD_GROUP_MESSAGE, requestParams);
    }

    private void sendSyncHttpRequest(String baseURL, String path, MultiValueMap requestParams) {
        Mono<Response> response = WebClient.create(baseURL).post()
                .uri(path)
                .syncBody(requestParams)
                .retrieve()
                .bodyToMono(Response.class);
        Response res = response.block();
        assert res != null;
        log.info("Сохранил сообщение" + res.toString());
    }

}
