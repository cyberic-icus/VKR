package ru.rsreu.websocketserverservice.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import lombok.extern.java.Log;
import ru.rsreu.websocketserverservice.constant.CONSTANT;
import ru.rsreu.websocketserverservice.service.MessageService;
import ru.rsreu.websocketserverservice.service.impl.MessageServiceImpl;

import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

@Log
public class WebSocketServerHandler extends SimpleChannelInboundHandler<Object> {

    private WebSocketServerHandshaker handShaker;

    private MessageService messageService;

    public WebSocketServerHandler() {
        this.messageService = new MessageServiceImpl();
    }

    /**
     * Ответ клиенту
     *
     * @param ctx
     * @param req
     * @param res
     */
    private static void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, DefaultFullHttpResponse res) {

        // Ответ клиенту
        if (res.getStatus().code() != 200) {
            ByteBuf buf = Unpooled.copiedBuffer(res.getStatus().toString(), CharsetUtil.UTF_8);
            res.content().writeBytes(buf);
            buf.release();
        }

        // Если не Keep-Alive, закрыть соединение
        ChannelFuture f = ctx.channel().writeAndFlush(res);
        if (!HttpHeaders.isKeepAlive(req) || res.getStatus().code() != 200) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }

    /**
     * Обработка полученного сообщения
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        // Традиционный HTTP доступ
        if (msg instanceof FullHttpRequest) {
            log.info("========= Http запрос доступа=========");
            handleHttpRequest(ctx, ((FullHttpRequest) msg));
        }
        // WebSocket доступ
        else if (msg instanceof WebSocketFrame) {
            log.info("========= WebSocket запрос доступа=========");
            handleWebSocketFrame(ctx, (WebSocketFrame) msg);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    /**
     * Обработка HTTP запроса
     *
     * @param ctx
     * @param req
     */
    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) {
        // Декодирование HTTP не удалось, возвращаем HTTP ошибку
        if (!req.getDecoderResult().isSuccess() || (!"websocket".equals(req.headers().get("Upgrade")))) {
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HTTP_1_1, BAD_REQUEST));
            return;
        }
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
                getWebSocketLocation(req), null, false);
        handShaker = wsFactory.newHandshaker(req);
        if (handShaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedWebSocketVersionResponse(ctx.channel());
        } else {
            handShaker.handshake(ctx.channel(), req);
        }
        messageService.onOpen(ctx, req);
    }

    /**
     * Обработка WebSocket сообщения
     *
     * @param ctx
     * @param frame
     */
    private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {

        // Проверка, является ли это командой для закрытия соединения
        if (frame instanceof CloseWebSocketFrame) {
            messageService.onClose();
            handShaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
            return;
        }
        // Проверка, является ли это ping сообщением
        if (frame instanceof PingWebSocketFrame) {
            ctx.write(new PongWebSocketFrame(frame.content().retain()));
            return;
        }
        // Проверка, является ли это текстовым сообщением
        if (!(frame instanceof TextWebSocketFrame)) {
            log.info("======= Поддерживаются только текстовые сообщения, бинарные сообщения не поддерживаются ========");
            throw new UnsupportedOperationException(String.format("%s типы фреймов не поддерживаются", frame.getClass().getName()));
        }

        String jsonMessage = ((TextWebSocketFrame) frame).text();
        messageService.onMessage(ctx, jsonMessage);
    }

    private String getWebSocketLocation(FullHttpRequest req) {
        String location = req.headers().get(HttpHeaders.Names.HOST) + CONSTANT.WEBSOCKET_PATH;
        return "ws://" + location;
    }

}