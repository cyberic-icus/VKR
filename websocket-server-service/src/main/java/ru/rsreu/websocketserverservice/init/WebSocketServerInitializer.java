package ru.rsreu.websocketserverservice.init;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;
import ru.rsreu.websocketserverservice.handler.WebSocketServerHandler;


public class WebSocketServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel e) throws Exception {

        // protobuf обработчик
//        e.pipeline().addLast("frameDecoder", new ProtobufVarint32FrameDecoder());// используется для решения проблемы полу-кадра и липких кадров перед декодированием
        // здесь нужно передать настраиваемый protobuf defaultInstance
        // e.pipeline().addLast("protobufDecoder", new ProtobufDecoder();
        // декодирование запросов и ответов в HTTP сообщения
        e.pipeline().addLast("http-codec", new HttpServerCodec());
        // HttpObjectAggregator: объединяет несколько частей HTTP сообщения в одно полное сообщение
        e.pipeline().addLast("aggregator", new HttpObjectAggregator(65536));
        // ChunkedWriteHandler: отправляет HTML5 файлы клиенту
        e.pipeline().addLast("http-chunked", new ChunkedWriteHandler());
        // добавляем наш собственный метод обработки данных в конвейер
        e.pipeline().addLast("handler", new WebSocketServerHandler());

    }
}

