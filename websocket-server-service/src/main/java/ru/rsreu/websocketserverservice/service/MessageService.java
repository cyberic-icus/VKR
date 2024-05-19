package ru.rsreu.websocketserverservice.service;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

public interface MessageService {
    /**
     * Метод обратного вызова при установлении WebSocket соединения
     *
     * @param ctx
     * @param req
     */
    void onOpen(ChannelHandlerContext ctx, FullHttpRequest req);

    /**
     * Метод обратного вызова при закрытии WebSocket соединения
     */
    void onClose();

    /**
     * Метод обратного вызова при получении сообщения
     *
     * @param ctx
     * @param jsonMessage
     */
    void onMessage(ChannelHandlerContext ctx, String jsonMessage);
}