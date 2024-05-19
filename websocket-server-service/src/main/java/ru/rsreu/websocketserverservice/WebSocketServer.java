package ru.rsreu.websocketserverservice;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.java.Log;
import ru.rsreu.websocketserverservice.constant.CONSTANT;
import ru.rsreu.websocketserverservice.init.WebSocketServerInitializer;

import javax.net.ssl.SSLException;
import java.security.cert.CertificateException;

@Log
public class WebSocketServer {

    public static void main(String[] args) throws CertificateException, SSLException {
        new WebSocketServer().run();
//        MessageServiceImpl messageService = new MessageServiceImpl();
//        Message message = new Message();
//        message.setFromId(1);
//        message.setToId(2);
//        message.setTime(Timestamp.valueOf(sdf.format(new Date())));
//        message.setContent("234");
//        message.setType(1);
//        messageService.saveMessage(message);
    }

//    public void initNetty() {
//        new Thread() {
//            public void run() {
//                try {
//                    new WebSocketServer().run();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();
//    }

    public void run() {

        log.info("---------------Netty Server Start------------------");
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap sb = new ServerBootstrap();
            sb.group(bossGroup, workerGroup);
            sb.channel(NioServerSocketChannel.class);
            sb.childHandler(new WebSocketServerInitializer());
            log.info("-----------------Netty Server is waiting for connect------------");
            log.info("Netty port: " + CONSTANT.NETTY_PORT);
            Channel channel = sb.bind(CONSTANT.NETTY_PORT).sync().channel();
            channel.closeFuture().sync();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
