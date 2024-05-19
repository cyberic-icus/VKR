package ru.rsreu.websocketserverservice.constant;

import io.netty.channel.Channel;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;


public class Global {

    public static Map<Integer, Channel> onLineList = new HashMap<>();

    public static Integer onLineNumber = 0;

//    public static Jedis jedis = JedisPoolUtil.getJedis();

    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
}
