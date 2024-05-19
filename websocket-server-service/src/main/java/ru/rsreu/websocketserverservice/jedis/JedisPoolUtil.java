package ru.rsreu.websocketserverservice.jedis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import ru.rsreu.websocketserverservice.constant.CONSTANT;

/**
 * Создание пула соединений Jedis, управление соединениями jedis
 */
public class JedisPoolUtil {
    private static JedisPool pool;

    /**
     * Создание пула соединений и настройка соответствующих параметров
     */
    private static void createJedisPool() {
        JedisPoolConfig config = new JedisPoolConfig();
        // Установка максимального количества соединений
        config.setMaxTotal(CONSTANT.MAX_TOTAL);
        // Установка максимального времени ожидания (в мс)
        config.setMaxWaitMillis(CONSTANT.MAX_WAIT);
        // Установка минимального количества свободных соединений
        config.setMinIdle(CONSTANT.MAX_IDLE);
        // Создание пула соединений
        pool = new JedisPool(config, CONSTANT.REDIS_ADDRESS, CONSTANT.REDIS_PORT);
    }

    /**
     * Синхронизированная инициализация в многопоточном окружении
     */
    private static synchronized void poolInit() {
        if (pool == null) {
            createJedisPool();
        }
    }

    /**
     * Получение соединения Jedis
     *
     * @return
     */
    public static Jedis getJedis() {
        if (pool == null) {
            poolInit();
        }
        return pool.getResource();
    }

}