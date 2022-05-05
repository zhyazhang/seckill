package com.aifurion.seckill.common.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.PostConstruct;

/**
 * @author ：zzy
 * @description：TODO
 * @date ：2022/5/5 13:29
 */

@Component
public class RedisPool {


    private static JedisPool pool;

    @Value("${redis.maxIdle}")
    private Integer maxTotal;

    @Value("${redis.maxTotal}")
    private Integer maxIdle;

    @Value("${redis.maxWait}")
    private Integer maxWait;

    @Value("${redis.testOnBorrow}")
    private Boolean testOnBorrow;

    @Value("${redis.timeout}")
    private Integer timeout;

    @Value("${spring.redis.host}")
    private String redisIP;

    @Value("${spring.redis.port}")
    private Integer redisPort;


    // 类加载到 jvm 时直接初始化连接池


    @PostConstruct
    public void init() {

        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setTestOnBorrow(testOnBorrow);
        config.setBlockWhenExhausted(true);
        config.setMaxWaitMillis(maxWait);

        pool = new JedisPool(config, redisIP, redisPort, timeout);
    }


    public static Jedis getJedis() {
        return pool.getResource();
    }

    public static void jedisPoolClose(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }
}
